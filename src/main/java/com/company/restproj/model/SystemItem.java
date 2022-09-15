package com.company.restproj.model;

import com.company.restproj.modeldb.SystemItemDB;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SystemItem {
    private String id;
    private String url;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date date;
    private String parentId;
    private String type;
    private Long size;
    private List<SystemItem> children;

    public SystemItem(SystemItemDB itemDB) {
        id = itemDB.getId();
        url = itemDB.getUrl();
        date = new Date(itemDB.getDate().getTime());
        parentId = itemDB.getParentId();
        type = itemDB.getType();
        size = itemDB.getSize();
        children = new ArrayList<>();
    }
}
