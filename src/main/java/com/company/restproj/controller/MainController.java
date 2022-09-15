package com.company.restproj.controller;


import com.company.restproj.config.JsonValidate;
import com.company.restproj.config.ValidateInputData;
import com.company.restproj.logic.SystemItemLogic;
import com.company.restproj.model.Error;
import com.company.restproj.model.SystemItem;
import com.company.restproj.model.SystemItemHistoryResponse;
import com.company.restproj.model.SystemItemImportRequest;
import com.ethlo.time.DateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RestController
public class MainController {

    private final Error errorInvalidJson = new Error(400, "Validation Failed");
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
            return ResponseEntity.badRequest().body(errorInvalidJson);
        }
        if (!jsonValidate.isJsonValid(jsonNode, "SystemItemImportRequest"))
            return ResponseEntity.badRequest().body(errorInvalidJson);
        else{
            SystemItemImportRequest imp = null;
            try {
                imp = mapper.treeToValue(jsonNode, SystemItemImportRequest.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body(errorInvalidJson);
            }
            if (!validateInputData.inputItemsValidate(imp))
                return ResponseEntity.badRequest().body(errorInvalidJson);
            systemItemLogic.parseSystemItemImportRequest(imp);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteFiles(@PathVariable String id, @RequestParam String date){
        //проверить дату
        Date date1 = validateInputData.dateValidate(date);
        if (date1 == null)
            return new ResponseEntity<>(errorInvalidJson, HttpStatus.BAD_REQUEST);

        if (!validateInputData.findItem(id))
            return new ResponseEntity<>(errorFileNotFound, HttpStatus.NOT_FOUND);

        systemItemLogic.deleteItemFromDB(id, date1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<SystemItem> getFiles(@PathVariable String id) {
        if (!validateInputData.findItem(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        SystemItem item = systemItemLogic.getNode(id);
        System.out.println(item.getDate().toString());
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/updates")
    public ResponseEntity<SystemItemHistoryResponse> getFiles24Hours(@RequestParam String date) {
        Date date1 = validateInputData.dateValidate(date);
        if (date1 == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        SystemItemHistoryResponse response = systemItemLogic.getItems24(date1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/node/{id}/history")
    public ResponseEntity<?> getHistory(@PathVariable String id, @RequestParam String dateStart, @RequestParam String dateEnd) {
        Date date1 = validateInputData.dateValidate(dateStart);
        if (date1 == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Date date2 = validateInputData.dateValidate(dateEnd);
        if (date2 == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(HttpStatus.LOCKED);
    }

}
