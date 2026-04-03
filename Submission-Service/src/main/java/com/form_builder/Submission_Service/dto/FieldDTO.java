package com.form_builder.Submission_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO {

    private String id;

    private String name;
    private String label;
    private String type;
    private boolean required;
    private Map<String, Object> config;

}
