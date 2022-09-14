package com.company.restproj.jsonvalidate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class JsonValidate {
    private final String SCHEMA_PATH;
    private final ObjectMapper mapper = new ObjectMapper();

    {
        Path path = Paths.get("classes/model/json-schema.json");
        if(Files.exists(path))
            SCHEMA_PATH = "classes/model/json-schema.json";
        else
            SCHEMA_PATH = "src/main/resources/model/json-schema.json";
    }

    public boolean isJsonValid(JsonNode json, String typeOfJson) {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        JsonNode schemaNode = null;
        try {
            InputStream schema = new FileInputStream(SCHEMA_PATH);
            schemaNode = mapper.readTree(schema).get(typeOfJson);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        JsonSchema jsonSchema = schemaFactory.getSchema(schemaNode);
        if (jsonSchema.validate(json).size() > 0)
            return false;
        else
            return true;
    }
}
