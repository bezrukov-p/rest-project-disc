package com.company.restproj.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SystemItem {
    private String id;
    private String url;
    private Date date;
    private String parentId;
    private String type;
    private Long size;
}
