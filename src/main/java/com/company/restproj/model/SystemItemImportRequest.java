package com.company.restproj.model;


import com.ethlo.time.DateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class SystemItemImportRequest {
    @NonNull
    private List<SystemItemImport> items;
    @NonNull
    private Date updateDate;
}
