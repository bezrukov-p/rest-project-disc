package com.company.restproj.model;

import com.company.restproj.modeldb.SystemItemDB;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SystemItemHistoryResponse {
    List<SystemItemHistoryUnit> units;

    public SystemItemHistoryResponse(List<SystemItemDB> itemsDB) {
        units = new ArrayList<>();
        if (!(itemsDB == null)){
            for (SystemItemDB itemDB: itemsDB) {
                units.add(new SystemItemHistoryUnit(itemDB));
            }
        }
    }
}
