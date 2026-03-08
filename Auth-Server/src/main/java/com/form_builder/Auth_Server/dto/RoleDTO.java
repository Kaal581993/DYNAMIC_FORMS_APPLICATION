package com.form_builder.Auth_Server.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class RoleDTO {
    private UUID id;
    private String name;
    private String description;

}
