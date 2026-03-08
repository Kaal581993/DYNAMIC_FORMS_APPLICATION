package com.form_builder.Form_Service.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
public class FieldDefinition {
    private String id;
    private String name;
    private String label;
    private FieldType type;
    private boolean required;

    private Map<String, Object> config;
}
