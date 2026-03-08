package com.form_builder.User_Service.mapper;

import com.form_builder.User_Service.dto.UserResponse;
import com.form_builder.User_Service.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());

        return response;
    }

}
