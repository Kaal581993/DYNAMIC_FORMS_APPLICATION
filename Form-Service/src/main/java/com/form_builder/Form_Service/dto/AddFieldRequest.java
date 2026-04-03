package com.form_builder.Form_Service.dto;


import com.form_builder.Form_Service.model.FieldDefinition;
import com.form_builder.Form_Service.model.FieldType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFieldRequest {


    private String id;

    @NotBlank
    private String name;
    private String description;

    private String label;
    private List<FieldDefinition> fields;

    private FieldType type;
    private boolean required;

    private Map<String, Object> config;

    private Integer version;
    private Boolean active;

    private LocalDateTime createdAt;

    private List<FieldRolePermissionRequest> rolePermissions;
}
