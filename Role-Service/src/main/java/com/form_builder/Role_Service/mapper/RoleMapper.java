package com.form_builder.Role_Service.mapper;

import com.form_builder.Role_Service.dto.RoleResponse;
import com.form_builder.Role_Service.model.Role;

public class RoleMapper {

    public static RoleResponse toResponse(Role role) {

        RoleResponse response = new RoleResponse();

        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());

        return response;
    }

}
