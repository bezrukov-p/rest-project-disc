package com.company.restproj.jsonvalidate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonValidate {
    private final String SCHEMA_PATH = "src/main/resources/model/json-schema.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public boolean isJsonValid(JsonNode json, String typeOfJson) {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        JsonNode schemaNode = null;
        try {
            schemaNode = mapper.readTree(new File(SCHEMA_PATH)).get("SystemItemImportRequest");
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
