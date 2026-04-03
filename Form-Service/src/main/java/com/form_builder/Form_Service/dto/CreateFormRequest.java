package com.form_builder.Form_Service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFormRequest {

    private String id;

    @NotBlank
    private String name;
    private String description;
    private String ownerEmail;
    private LocalDateTime createdAt;

    private List<FormRolePermissionRequest> rolePermissions;
}
