package com.company.restproj.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SystemItemImport {
    @NonNull
    private String id;
    private String url;
    private String parentId;
    @NonNull
    private String type;
    private Long size;
}
