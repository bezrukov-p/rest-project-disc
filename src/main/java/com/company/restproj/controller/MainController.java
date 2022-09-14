package com.company.restproj.controller;


import com.company.restproj.RestprojApplication;
import com.company.restproj.configure.ShutdownManager;
import com.company.restproj.jsonvalidate.JsonValidate;
import com.company.restproj.model.Error;
import com.company.restproj.model.SystemItemImportRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {
    @Autowired
    private ShutdownManager shutdownManager;

    private final Error errorInvalidJson = new Error(400, "Validation Failed");

    @Autowired
    private JsonValidate jsonValidate;

    private ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/imports")
    public ResponseEntity<Error> uploadFiles(@RequestBody JsonNode json) throws JsonProcessingException {
        if (!jsonValidate.isJsonValid(json, "SystemItemImportRequest"))
            return ResponseEntity.badRequest().body(errorInvalidJson);
        else{
            SystemItemImportRequest imp = mapper.treeToValue(json, SystemItemImportRequest.class);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFile(){
    }

    @GetMapping("/nodes/{id}")
    public void getFile() {

    }

    @PostMapping("/down")
    public void shutdownContext() {
        shutdownManager.initiateShutdown(0);
    }



}
