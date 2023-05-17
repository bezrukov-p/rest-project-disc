package com.company.restproj.config;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Component
public class JsonValidate {
    private final ObjectMapper mapper = new ObjectMapper();

    public boolean isJsonValid(JsonNode json, String typeOfJson) {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonNode schemaNode = null;
        ClassPathResource resource = new ClassPathResource("json-schema.json");
        try {
            InputStream schema = resource.getInputStream();
            schemaNode = mapper.readTree(schema).get(typeOfJson);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        JsonSchema jsonSchema = schemaFactory.getSchema(schemaNode);
        Set<ValidationMessage> resValid = jsonSchema.validate(json);
        if (resValid.size() > 0) {
            for (ValidationMessage message: resValid){
                System.out.println(message.getMessage());
            }
            return false;
        }
        else
            return true;
    }
}
