package com.bezrukov.restproj.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class SystemItemImportRequest {
    @NonNull
    private List<SystemItemImport> items;
    @NonNull
    private LocalDateTime updateDate;
}
