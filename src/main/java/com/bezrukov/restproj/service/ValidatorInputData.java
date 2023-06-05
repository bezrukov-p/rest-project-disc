package com.bezrukov.restproj.service;


import com.bezrukov.restproj.dto.SystemItemImport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ValidatorInputData {
    private final ObjectMapper objectMapper;
    @Autowired
    public ValidatorInputData(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    public boolean isJsonValidByJsonSchema(String jsonString, String typeOfJson) throws IOException {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            return false;
        }

        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        ClassPathResource resource = new ClassPathResource("json-schema.json");
        InputStream schema = resource.getInputStream();
        JsonNode schemaNode = objectMapper.readTree(schema).get(typeOfJson);
        JsonSchema jsonSchema = schemaFactory.getSchema(schemaNode);

        Set<ValidationMessage> validationMessages = jsonSchema.validate(jsonNode);
        return validationMessages.size() == 0;
    }

    public boolean areInputDataFieldsValid(List<SystemItemImport> importItems) {
        Set<String> folderIds = new HashSet<>();
        Set<String> fileIds = new HashSet<>();

        for (SystemItemImport item : importItems) {
            if (folderIds.contains(item.getId()) || fileIds.contains(item.getId()))
                return false;
            if (item.getType().equals("FOLDER")) {
                if (!(item.getSize() == null)
                        || !(item.getUrl() == null))
                    return false;

                folderIds.add(item.getId());
            }
            else {
                if (item.getSize() == null
                        || !(item.getSize() > 0)
                        || item.getUrl() == null
                        || item.getUrl().length() > 255)
                    return false;
                fileIds.add(item.getId());
            }
        }

        return true;
    }
}
