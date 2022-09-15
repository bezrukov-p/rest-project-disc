package com.company.restproj.controller;


import com.company.restproj.config.JsonValidate;
import com.company.restproj.config.ValidateInputData;
import com.company.restproj.logic.SystemItemLogic;
import com.company.restproj.model.Error;
import com.company.restproj.model.SystemItem;
import com.company.restproj.model.SystemItemHistoryResponse;
import com.company.restproj.model.SystemItemImportRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class MainController {

    private final Error errorInvalidData = new Error(400, "Validation Failed");
    private final Error errorFileNotFound = new Error(404, "Item not found");

    @Autowired
    private JsonValidate jsonValidate;

    @Autowired
    SystemItemLogic systemItemLogic;

    @Autowired
    ValidateInputData validateInputData;

    private ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/imports")
    public ResponseEntity<Error> uploadFiles(@RequestBody String json){
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(errorInvalidData);
        }
        if (!jsonValidate.isJsonValid(jsonNode, "SystemItemImportRequest"))
            return ResponseEntity.badRequest().body(errorInvalidData);
        else{
            SystemItemImportRequest imp = null;
            try {
                imp = mapper.treeToValue(jsonNode, SystemItemImportRequest.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(errorInvalidData);
            }
            if (!validateInputData.inputItemsValidate(imp))
                return ResponseEntity.badRequest().body(errorInvalidData);
            systemItemLogic.parseSystemItemImportRequest(imp);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteFiles(@PathVariable String id, @RequestParam String date){
        Date date1 = validateInputData.dateValidate(date);
        if (date1 == null)
            return new ResponseEntity<>(errorInvalidData, HttpStatus.BAD_REQUEST);

        if (!validateInputData.findItem(id))
            return new ResponseEntity<>(errorFileNotFound, HttpStatus.NOT_FOUND);

        systemItemLogic.deleteItemFromDB(id, date1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity getFiles(@PathVariable String id) {
        if (!validateInputData.findItem(id))
            return new ResponseEntity(errorFileNotFound, HttpStatus.NOT_FOUND);
        SystemItem item = systemItemLogic.getNode(id);
        System.out.println(item.getDate());
        return new ResponseEntity(item, HttpStatus.OK);
    }

    @GetMapping("/updates")
    public ResponseEntity getFiles24Hours(@RequestParam String date) {
        Date date1 = validateInputData.dateValidate(date);
        if (date1 == null)
            return new ResponseEntity(errorInvalidData, HttpStatus.BAD_REQUEST);

        SystemItemHistoryResponse response = systemItemLogic.getItems24(date1);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/node/{id}/history")
    public ResponseEntity getHistory(@PathVariable String id, @RequestParam String dateStart, @RequestParam String dateEnd) {
        Date date1 = validateInputData.dateValidate(dateStart);
        if (date1 == null)
            return new ResponseEntity<>(errorInvalidData, HttpStatus.BAD_REQUEST);
        Date date2 = validateInputData.dateValidate(dateEnd);
        if (date2 == null)
            return new ResponseEntity<>(errorInvalidData, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(HttpStatus.LOCKED);
    }

}
