package com.company.restproj.model;

import com.company.restproj.modeldb.SystemItemDB;
import lombok.Data;

import java.util.Date;

@Data
public class SystemItemHistoryUnit {
    String id;
    String url;
    String parentId;
    String type;
    Long size;
    Date date;

    public SystemItemHistoryUnit(SystemItemDB itemDB) {
        id = itemDB.getId();
        url = itemDB.getUrl();
        parentId = itemDB.getParentId();
        type = itemDB.getType();
        size = itemDB.getSize();
        date = itemDB.getDate();
    }
}
