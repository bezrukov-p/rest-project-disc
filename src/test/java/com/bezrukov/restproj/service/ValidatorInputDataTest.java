package com.bezrukov.restproj.service;

import com.bezrukov.restproj.beans.JsonsAsStringsBuilder;
import com.bezrukov.restproj.beans.SystemItemImportBuilder;
import com.bezrukov.restproj.dto.SystemItemImport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Import({JsonsAsStringsBuilder.class, SystemItemImportBuilder.class})
class ValidatorInputDataTest {
    @Autowired
    private ValidatorInputData validatorInputData;
    @Autowired
    @Qualifier("validJsons")
    private List<String> validJsons;
    @Autowired
    @Qualifier("invalidJsons")
    private List<String> invalidJsons;

    @Autowired
    @Qualifier("validFieldsItemsImport")
    private List<SystemItemImport> validFieldsItemsImport;

    @Autowired
    @Qualifier("invalidFieldsItemsImport")
    private List<SystemItemImport> invalidFieldsItemsImport;

    private final String typeOfJson = "SystemItemImportRequest";

    @Test
    void validJsonsTest() throws IOException {
        for (String json: validJsons){
            Assertions.assertTrue(validatorInputData.isJsonValidByJsonSchema(json, typeOfJson));
        }
    }

    @Test
    void invalidJsonsTest() throws IOException {
        for (String json: invalidJsons) {
            Assertions.assertFalse(validatorInputData.isJsonValidByJsonSchema(json, typeOfJson));
        }
    }

    @Test
    void validFieldsItemsImportTest() {
        Assertions.assertTrue(validatorInputData.areInputDataFieldsValid(validFieldsItemsImport));
    }

    @Test
    void invalidFieldsItemsImportTest() {
        Assertions.assertFalse(validatorInputData.areInputDataFieldsValid(invalidFieldsItemsImport));
    }
}