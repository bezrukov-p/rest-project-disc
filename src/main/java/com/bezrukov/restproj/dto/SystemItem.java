package com.bezrukov.restproj.dto;

import com.bezrukov.restproj.entity.SystemItemEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SystemItem {
    private String id;
    private String url;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime date;
    private String parentId;
    private String type;
    private Long size;
    private List<SystemItem> children;

    public SystemItem(SystemItemEntity itemDB) {
        id = itemDB.getId();
        url = itemDB.getUrl();
        date = itemDB.getDate();
        parentId = itemDB.getParentId();
        type = itemDB.getType();
        size = itemDB.getSize();
        children = new ArrayList<>();
    }
}
