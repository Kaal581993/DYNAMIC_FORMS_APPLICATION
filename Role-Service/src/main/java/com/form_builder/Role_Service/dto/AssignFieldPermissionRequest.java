package com.form_builder.Role_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignFieldPermissionRequest {


    private String formId;
    private String fieldId;

    private Boolean visible;
    private Boolean editable;
    private Boolean requiredOverride;
    private Boolean masked;


}
