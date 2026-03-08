package com.form_builder.Submission_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO {

    private String id;

    private String name;

    private String description;

    private Integer version;

    private Boolean active;

    private boolean required;

    private List<FieldDTO> fields;

}
