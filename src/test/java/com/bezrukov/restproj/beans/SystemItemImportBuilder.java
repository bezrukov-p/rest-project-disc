package com.bezrukov.restproj.beans;

import com.bezrukov.restproj.dto.SystemItemImport;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class SystemItemImportBuilder {

    @Bean("validFieldsItemsImport")
    List<SystemItemImport> validItemsImport() {
        List<SystemItemImport> items = new ArrayList<>();
        items.add(new SystemItemImport("id1", null, null, "FOLDER", null));
        items.add(new SystemItemImport("id2", null, "id1", "FOLDER", null));
        items.add(new SystemItemImport("id3", "url1", "id1", "FILE", 100L));
        items.add(new SystemItemImport("id4", "url2", null, "FILE", 10L));

        return items;
    }

    @Bean("invalidFieldsItemsImport")
    List<SystemItemImport> invalidItemsImport() {
        List<SystemItemImport> items = new ArrayList<>();
        items.add(new SystemItemImport("id1", "url11", null, "FOLDER", null));
        items.add(new SystemItemImport("id2", null, "id1", "FOLDER", 1L));
        items.add(new SystemItemImport("id3", "url1", "id1", "FILE", null));
        items.add(new SystemItemImport("id2", null, null, "FILE", 10L));

        return items;
    }
}
