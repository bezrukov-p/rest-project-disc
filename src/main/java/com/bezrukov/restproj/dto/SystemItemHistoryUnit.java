package com.bezrukov.restproj.dto;

import com.bezrukov.restproj.entity.SystemItemEntity;
import com.bezrukov.restproj.entity.SystemItemHistoryEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemItemHistoryUnit {
    String id;
    String url;
    String parentId;
    String type;
    Long size;
    LocalDateTime date;

    public SystemItemHistoryUnit(SystemItemEntity item) {
        id = item.getId();
        url = item.getUrl();
        parentId = item.getParentId();
        type = item.getType();
        size = item.getSize();
        date = item.getDate();
    }

    public SystemItemHistoryUnit(SystemItemHistoryEntity item) {
        id = item.getId();
        url = item.getUrl();
        parentId = item.getParentId();
        type = item.getType();
        size = item.getSize();
        date = item.getDate();
    }
}
