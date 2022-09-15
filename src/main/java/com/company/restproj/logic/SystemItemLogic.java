package com.company.restproj.logic;

import com.company.restproj.model.SystemItem;
import com.company.restproj.model.SystemItemHistoryResponse;
import com.company.restproj.model.SystemItemImport;
import com.company.restproj.model.SystemItemImportRequest;
import com.company.restproj.modeldb.SystemItemDB;
import com.company.restproj.repository.SystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SystemItemLogic {
    @Autowired
    SystemItemRepository systemItemRepository;

    public void parseSystemItemImportRequest(SystemItemImportRequest imp) {

        for (SystemItemImport item: imp.getItems()){
            if (item.getType().equals("FOLDER")) {
                if (systemItemRepository.existsById(item.getId())){
                    SystemItemDB itemFromDB = systemItemRepository.findById(item.getId()).get();
                    resizeParentsItems(itemFromDB, imp.getUpdateDate(), -1);
                    SystemItemDB itemDB = new SystemItemDB(item, imp.getUpdateDate());
                    itemDB.setSize(itemFromDB.getSize());
                    systemItemRepository.save(itemDB);
                    resizeParentsItems(itemDB, imp.getUpdateDate(), 1);
                }
                else {
                    SystemItemDB itemDB = new SystemItemDB(item, imp.getUpdateDate());
                    itemDB.setSize(0L);
                    systemItemRepository.save(itemDB);
                }
            }
        }
        for (SystemItemImport item: imp.getItems()){
            if (item.getType().equals("FILE")) {
                if (systemItemRepository.existsById(item.getId())){
                    SystemItemDB itemFromDB = systemItemRepository.findById(item.getId()).get();
                    resizeParentsItems(itemFromDB, imp.getUpdateDate(), -1);
                    SystemItemDB itemDB = new SystemItemDB(item, imp.getUpdateDate());
                    itemDB.setSize(itemFromDB.getSize());
                    systemItemRepository.save(itemDB);
                    resizeParentsItems(itemDB, imp.getUpdateDate(), 1);
                }
                else {
                    SystemItemDB itemDB = new SystemItemDB(item, imp.getUpdateDate());
                    systemItemRepository.save(itemDB);
                    resizeParentsItems(itemDB, imp.getUpdateDate(), 1);
                }
            }
        }
    }

    public void deleteItemFromDB(String id, Date date) {
        SystemItemDB itemDB = systemItemRepository.findById(id).get();
        if (itemDB.getType().equals("FILE")) {
            resizeParentsItems(itemDB, date, -1);
            systemItemRepository.deleteById(id);
        }
        else {
            resizeParentsItems(itemDB, date, -1);
            deleteItemAndChildrenRecursive(itemDB);
        }
    }

    public void deleteItemAndChildrenRecursive(SystemItemDB itemDB) {
        List<SystemItemDB> children = systemItemRepository.getSystemItemDBByParentId(itemDB.getId());
        if (children != null) {
            for (SystemItemDB item : children) {
                deleteItemAndChildrenRecursive(item);
            }
        }
        systemItemRepository.deleteById(itemDB.getId());
    }

    public SystemItem getNode(String id) {
        SystemItemDB itemDB;
        itemDB = systemItemRepository.findById(id).get();
        SystemItem item = new SystemItem(itemDB);
        fillChildrenRecursive(item);
        return item;
    }

    public void fillChildrenRecursive(SystemItem item) {
        if (item.getType().equals("FILE")){
            item.setChildren(null);
            return;
        }
        List<SystemItemDB> childrenDB = systemItemRepository.getSystemItemDBByParentId(item.getId());
        List<SystemItem> children = new ArrayList<>();
        for (SystemItemDB itemDB: childrenDB) {
            SystemItem tmp = new SystemItem(itemDB);
            children.add(tmp);
        }
        item.setChildren(children);
        childrenDB.clear();
        for (SystemItem child: children)
            fillChildrenRecursive(child);
    }

    public void resizeParentsItems(SystemItemDB item, Date date, int plusOrMinus) {
        Long size = item.getSize();
        size = (plusOrMinus > 0) ? size : -size;
        String parentId = item.getParentId();
        while (parentId != null){
            SystemItemDB itemDB = systemItemRepository.findById(parentId).get();
            itemDB.setDate(date);
            itemDB.setSize(itemDB.getSize() + size);
            systemItemRepository.save(itemDB);
            parentId = itemDB.getParentId();
        }
    }

    public SystemItemHistoryResponse getItems24(Date date) {
        long day = 86400000L;
        Date date1 = new Date(date.getTime());
        date1.setTime(date.getTime() - day);
        List<SystemItemDB> itemsDB = systemItemRepository.getSystemItemDBByDateBetween(date1, date);
        SystemItemHistoryResponse response = new SystemItemHistoryResponse(itemsDB);
        return response;
    }




}
