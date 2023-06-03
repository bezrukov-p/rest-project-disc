package com.bezrukov.restproj.dto;

import com.bezrukov.restproj.entity.SystemItemEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SystemItemHistoryResponse {
    List<SystemItemHistoryUnit> units;

    public SystemItemHistoryResponse(List<SystemItemEntity> itemsDB) {
        units = new ArrayList<>();
        if (!(itemsDB == null)){
            for (SystemItemEntity itemDB: itemsDB) {
                units.add(new SystemItemHistoryUnit(itemDB));
            }
        }
    }
}
