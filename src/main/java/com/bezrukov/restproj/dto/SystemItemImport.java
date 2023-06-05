package com.bezrukov.restproj.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SystemItemImport {
    @NonNull
    private String id;
    private String url;
    private String parentId;
    @NonNull
    private String type;
    private Long size;

    public SystemItemImport(@NonNull String id, String url, String parentId, @NonNull String type, Long size) {
        this.id = id;
        this.url = url;
        this.parentId = parentId;
        this.type = type;
        this.size = size;
    }
}
