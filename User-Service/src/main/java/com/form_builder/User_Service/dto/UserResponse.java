package com.form_builder.User_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {


    private UUID Id;

    private List<UUID> roleId;
    private String firstName;
    private String lastName;

    private String userName;

    private String email;

    private String password;

    private Boolean active;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;
}
