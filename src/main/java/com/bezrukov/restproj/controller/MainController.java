package com.bezrukov.restproj.controller;


import com.bezrukov.restproj.repository.SystemItemRepository;
import com.bezrukov.restproj.service.SystemItemService;
import com.bezrukov.restproj.service.ValidatorInputData;
import com.bezrukov.restproj.dto.Error;
import com.bezrukov.restproj.dto.SystemItem;
import com.bezrukov.restproj.dto.SystemItemHistoryResponse;
import com.bezrukov.restproj.dto.SystemItemImportRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
public class MainController {

    private final Error errorInvalidData = new Error(400, "Validation Failed");
    private final Error errorFileNotFound = new Error(404, "Item not found");
    private final ValidatorInputData validatorInputData;
    private final SystemItemService systemItemService;
    private final ObjectMapper objectMapper;
    private final SystemItemRepository systemItemRepository;


    @Autowired
    public MainController(ValidatorInputData validatorInputData,
                          SystemItemService systemItemService,
                          ObjectMapper objectMapper,
                          SystemItemRepository systemItemRepository) {
        this.validatorInputData = validatorInputData;
        this.systemItemService = systemItemService;
        this.objectMapper = objectMapper;
        this.systemItemRepository = systemItemRepository;
    }


    @PostMapping("/imports")
    public ResponseEntity<Error> uploadFiles(@RequestBody String json){
        SystemItemImportRequest importObject;
        try {
            if (!validatorInputData.isJsonValidByJsonSchema(json, "SystemItemImportRequest"))
                return ResponseEntity.badRequest().body(errorInvalidData);
            importObject = objectMapper.readValue(json, SystemItemImportRequest.class);
            if (!validatorInputData.areInputDataFieldsValid(importObject.getItems())
                    || !systemItemService.areInputDataValidForWritingToDB(importObject.getItems()))
                return ResponseEntity.badRequest().body(errorInvalidData);
        } catch (IOException e) {
            // ТУТ ЛОГИРОВАНИЕ
            System.out.println("не найдена схема");
            return ResponseEntity.internalServerError().build();
        }

        systemItemService.parseSystemItemImportRequest(importObject);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteFiles(@PathVariable String id, @RequestParam String date){
        LocalDateTime updateTime;
        try {
            updateTime = LocalDateTime.parse(date);
        }
        catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(errorInvalidData);
        }

        if (!systemItemRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorFileNotFound);

        systemItemService.deleteItemFromDB(id, updateTime);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<?> getFiles(@PathVariable String id) {
        if (!systemItemRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorFileNotFound);
        SystemItem itemResponse = systemItemService.getNode(id);
        return ResponseEntity.ok(itemResponse);
    }

    @GetMapping("/updates")
    public ResponseEntity<?> getFiles24Hours(@RequestParam String date) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(date);
        }
        catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(errorInvalidData);
        }

        SystemItemHistoryResponse response = systemItemService.getItemsLast24hours(dateTime);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/node/{id}/history")
    public ResponseEntity<?> getHistory(@PathVariable String id, @RequestParam String dateStart, @RequestParam String dateEnd) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        try {
            startDateTime = LocalDateTime.parse(dateStart);
            endDateTime = LocalDateTime.parse(dateEnd);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(errorInvalidData);
        }



        return ResponseEntity.noContent().build();
    }

}
