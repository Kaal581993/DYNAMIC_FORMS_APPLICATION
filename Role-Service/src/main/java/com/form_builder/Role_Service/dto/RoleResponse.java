package com.form_builder.Role_Service.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleResponse {
    private UUID id;
    private String name;
    private String description;

    private Boolean active;

    private LocalDateTime createdAt;
}
