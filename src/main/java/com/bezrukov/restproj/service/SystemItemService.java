package com.bezrukov.restproj.service;

import com.bezrukov.restproj.dto.SystemItem;
import com.bezrukov.restproj.dto.SystemItemHistoryResponse;
import com.bezrukov.restproj.dto.SystemItemImportRequest;
import com.bezrukov.restproj.entity.SystemItemEntity;
import com.bezrukov.restproj.dto.SystemItemImport;
import com.bezrukov.restproj.entity.SystemItemHistoryEntity;
import com.bezrukov.restproj.repository.SystemItemHistoryRepository;
import com.bezrukov.restproj.repository.SystemItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class SystemItemService {

    private final SystemItemRepository systemItemRepository;
    private final SystemItemHistoryRepository systemItemHistoryRepository;

    @Autowired
    public SystemItemService(SystemItemRepository systemItemRepository, SystemItemHistoryRepository systemItemHistoryRepository) {
        this.systemItemRepository = systemItemRepository;
        this.systemItemHistoryRepository = systemItemHistoryRepository;
    }

    public boolean areInputDataValidForWritingToDB(List<SystemItemImport> importItems) {
        Set<String> folderIds = new HashSet<>();
        Set<String> fileIds = new HashSet<>();

        for (SystemItemImport item : importItems) {
            if ("FOLDER".equals(item.getType()))
                folderIds.add(item.getId());
            else
                fileIds.add(item.getId());
        }

        //проверка на существование parent для каждого элемента
        for (SystemItemImport item: importItems) {
            String parentId = item.getParentId();
            if (fileIds.contains(parentId))
                return false;
            if (parentId == null)
                continue;
            if (folderIds.contains(parentId))
                continue;
            if (!systemItemRepository.existsById(parentId))
                return false;

            SystemItemEntity itemDB = systemItemRepository.getReferenceById(parentId);
            if ("FILE".equals(itemDB.getType()))
                return false;
        }

        //проверка на соответствие типов данных
        for (SystemItemImport item: importItems) {
            if (systemItemRepository.existsById(item.getId())){
                SystemItemEntity itemDB = systemItemRepository.getReferenceById(item.getId());
                if (!itemDB.getType().equals(item.getType()))
                    return false;
            }
        }

        return true;
    }

    public void parseSystemItemImportRequest(SystemItemImportRequest imp) {
        for (SystemItemImport item: imp.getItems()){
            if (item.getType().equals("FOLDER")) {
                if (systemItemRepository.existsById(item.getId())){
                    SystemItemEntity itemFromDB = systemItemRepository.getReferenceById(item.getId());

                    systemItemHistoryRepository.save(new SystemItemHistoryEntity(itemFromDB));

                    resizeParentItems(itemFromDB, imp.getUpdateDate(), -1);
                    SystemItemEntity itemDB = new SystemItemEntity(item, imp.getUpdateDate());
                    itemDB.setSize(itemFromDB.getSize());
                    systemItemRepository.save(itemDB);
                    resizeParentItems(itemDB, imp.getUpdateDate(), 1);
                }
                else {
                    SystemItemEntity itemDB = new SystemItemEntity(item, imp.getUpdateDate());
                    itemDB.setSize(0L);
                    systemItemRepository.save(itemDB);
                }
            }
        }
        for (SystemItemImport item: imp.getItems()){
            if (item.getType().equals("FILE")) {
                if (systemItemRepository.existsById(item.getId())){
                    SystemItemEntity itemFromDB = systemItemRepository.getReferenceById(item.getId());

                    systemItemHistoryRepository.save(new SystemItemHistoryEntity(itemFromDB));

                    resizeParentItems(itemFromDB, imp.getUpdateDate(), -1);
                    SystemItemEntity itemDB = new SystemItemEntity(item, imp.getUpdateDate());
                    itemDB.setSize(itemFromDB.getSize());
                    systemItemRepository.save(itemDB);
                    resizeParentItems(itemDB, imp.getUpdateDate(), 1);
                }
                else {
                    SystemItemEntity itemDB = new SystemItemEntity(item, imp.getUpdateDate());
                    systemItemRepository.save(itemDB);
                    resizeParentItems(itemDB, imp.getUpdateDate(), 1);
                }
            }
        }
    }

    private void resizeParentItems(SystemItemEntity item, LocalDateTime updateTime, int plusOrMinus) {
        Long size = item.getSize();
        size = (plusOrMinus > 0) ? size : -size;
        String parentId = item.getParentId();
        while (parentId != null){
            SystemItemEntity itemDB = systemItemRepository.getReferenceById(parentId);
            itemDB.setDate(updateTime);
            itemDB.setSize(itemDB.getSize() + size);
            systemItemRepository.save(itemDB);
            parentId = itemDB.getParentId();
        }
    }

    public void deleteItemFromDB(String id, LocalDateTime updateTime) {
        SystemItemEntity itemDB = systemItemRepository.getReferenceById(id);
        if ("FILE".equals(itemDB.getType())) {
            resizeParentItems(itemDB, updateTime, -1);
            systemItemRepository.deleteById(id);
            systemItemHistoryRepository.deleteAllById(id);
        }
        else {
            resizeParentItems(itemDB, updateTime, -1);
            deleteItemAndChildrenRecursive(itemDB);
        }
    }

    private void deleteItemAndChildrenRecursive(SystemItemEntity itemDB) {
        List<SystemItemEntity> children = systemItemRepository.getSystemItemEntitiesByParentId(itemDB.getId());
        if (children != null)
            children.forEach(this::deleteItemAndChildrenRecursive);

        systemItemRepository.deleteById(itemDB.getId());
        systemItemHistoryRepository.deleteAllById(itemDB.getId());
    }

    public SystemItem getNode(String id) {
        SystemItemEntity itemDB = systemItemRepository.getReferenceById(id);
        SystemItem item = new SystemItem(itemDB);
        fillChildrenRecursive(item);
        return item;
    }

    private void fillChildrenRecursive(SystemItem item) {
        if (item.getType().equals("FILE")){
            item.setChildren(null);
            return;
        }
        List<SystemItemEntity> childrenDB = systemItemRepository.getSystemItemEntitiesByParentId(item.getId());
        List<SystemItem> children = new ArrayList<>();
        for (SystemItemEntity itemDB: childrenDB) {
            SystemItem tmp = new SystemItem(itemDB);
            children.add(tmp);
        }
        item.setChildren(children);
        childrenDB.clear();
        for (SystemItem child: children)
            fillChildrenRecursive(child);
    }

    public SystemItemHistoryResponse getItemsLast24hours(LocalDateTime date) {
        LocalDateTime startDateTime = date.minusDays(1L);
        List<SystemItemEntity> itemsDB = systemItemRepository.
                getSystemItemEntitiesByDateGreaterThanEqualAndDateLessThanEqual(startDateTime, date);
        return new SystemItemHistoryResponse(itemsDB);
    }

}
