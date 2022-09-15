package com.company.restproj.config;

import com.company.restproj.model.SystemItemImport;
import com.company.restproj.model.SystemItemImportRequest;
import com.company.restproj.modeldb.SystemItemDB;
import com.company.restproj.repository.SystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class ValidateInputData {

    @Autowired
    SystemItemRepository systemItemRepository;

    public Date dateValidate(String date) {
        Calendar calendar;
        try {
            calendar = DatatypeConverter.parseDateTime(date);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return new Date(calendar.getTimeInMillis());
    }

    public boolean inputItemsValidate(SystemItemImportRequest imp) {
        //id не повторяются +
        //проверка даты -
        //проверка size -
        //родитель элемента только папка -
        //поле url при импорте папки всегда должно быть равно null +
        //размер поля url при импорте файла всегда должен быть меньше либо равным 255 +
        //поле size при импорте папки всегда должно быть равно null +
        //поле size для файлов всегда должно быть больше 0 +

        //
        Set<String> fileIds = new HashSet<>();
        Set<String> folderIds = new HashSet<>();





        for (SystemItemImport item: imp.getItems()) {
            //проверка на повторы +
            if (fileIds.contains(item.getId())
                    || folderIds.contains(item.getId()))
                return false;
            if (item.getType().equals("FOLDER")) {
                if (!(item.getSize() == null)
                        || !(item.getUrl() == null))
                    return false;

                folderIds.add(item.getId());
            }
            else {
                if (item.getSize() == null
                        || !(item.getSize() > 0)
                        || item.getUrl() == null
                        || item.getUrl().length() > 255)
                    return false;
                fileIds.add(item.getId());
            }
        }

        //проверка на существование parent для каждого элемента
        for (SystemItemImport item: imp.getItems()) {
            String parent = item.getParentId();
            if (parent == null)
                continue;
            if (folderIds.contains(parent))
                continue;
            if (!systemItemRepository.existsById(parent))
                return false;
            SystemItemDB itemDB = systemItemRepository.findById(parent).get();
            if (itemDB.getType().equals("FILE"))
                return false;
        }

        //проверка на соответствие типов данных
        for (SystemItemImport item: imp.getItems()) {
            if (systemItemRepository.existsById(item.getId())){
                SystemItemDB itemDB = systemItemRepository.findById(item.getId()).get();
                if (!itemDB.getType().equals(item.getType()))
                    return false;
            }
        }



        return true;
    }

    public boolean findItem(String id) {
        return systemItemRepository.existsById(id);
    }
}
