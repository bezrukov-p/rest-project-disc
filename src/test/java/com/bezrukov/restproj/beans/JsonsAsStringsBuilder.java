package com.bezrukov.restproj.beans;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@TestConfiguration
public class JsonsAsStringsBuilder {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean("validJsons")
    List<String> validItemsRequestAsStrings() throws IOException {
        List<String> jsons = new ArrayList<>();

        InputStream json = new ClassPathResource("valid_items_request.json").getInputStream();

        Iterator<JsonNode> iterator = objectMapper.readTree(json).elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            jsons.add(node.toString());
        }

        return jsons;
    }
    @Bean("invalidJsons")
    List<String> invalidItemsRequestAsStrings() throws IOException {
        List<String> jsons = new ArrayList<>();

        InputStream json = new ClassPathResource("invalid_items_request.json").getInputStream();

        Iterator<JsonNode> iterator = objectMapper.readTree(json).elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            jsons.add(node.toString());
        }

        return jsons;

    }
}
