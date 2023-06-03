package com.bezrukov.restproj.dto;

import com.bezrukov.restproj.entity.SystemItemEntity;
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

    public SystemItemHistoryUnit(SystemItemEntity itemDB) {
        id = itemDB.getId();
        url = itemDB.getUrl();
        parentId = itemDB.getParentId();
        type = itemDB.getType();
        size = itemDB.getSize();
        date = itemDB.getDate();
    }
}
