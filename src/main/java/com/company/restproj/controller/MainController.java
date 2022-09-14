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

import java.util.Date;

@RestController
public class MainController {
    @Autowired
    private ShutdownManager shutdownManager;

    private final Error errorInvalidJson = new Error(400, "Validation Failed");
    private final Error errorFileNotFound = new Error(404, "Item not found");

    @Autowired
    private JsonValidate jsonValidate;

    private ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/imports")
    public ResponseEntity<Error> uploadFiles(@RequestBody JsonNode json) throws JsonProcessingException {
        if (!jsonValidate.isJsonValid(json, "SystemItemImportRequest"))
            return ResponseEntity.badRequest().body(errorInvalidJson);
        else{
            SystemItemImportRequest imp = mapper.treeToValue(json, SystemItemImportRequest.class);
            /*
            приходит в произвольном порядке, нужно чтобы во входных данных система была цельной, и оставалась цельной
            в бд. только пэрент, нет детей. проверка даты?
            проверка на уникальность айдишек
            нет циклических зависимостей?
            проверка и вычисление/перевычисление size
            НУЖНО ОБНОВЛЯТЬ ДАТУ У РОДИТЕЛЬСКИХ ПАПОК ВСЕХ?
            */

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteFiles(@PathVariable(value = "id") String id, @RequestParam Date date){
        /*
        проверка на валидацию входных данных
        если удаляется папка, то удаляются все дочерние элементы
        возвращается ошибка 404!!! как вернуть её с контентом?
        вычисление новых размеров
        */
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nodes/{id}")
    public void getFiles(@PathVariable String id) {
        /*
        получить в виде массива все элементы нода
        */
    }

    @GetMapping("/updates")
    public void getFiles24Hours(@RequestParam Date date) {
        /*
            ТОЛЬКО ДЛЯ ОДНОГО ЭЛЕМЕНТА
        */
    }

    @GetMapping("/node/{id}/history")
    public void getHistory(@PathVariable String id, @RequestParam Date dateStart, @RequestParam Date dateEnd) {

    }

    @PostMapping("/down")
    public void shutdownContext() {
        shutdownManager.initiateShutdown(0);
    }



}
