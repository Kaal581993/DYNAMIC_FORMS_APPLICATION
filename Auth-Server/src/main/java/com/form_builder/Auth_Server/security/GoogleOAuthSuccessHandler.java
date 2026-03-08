package com.form_builder.Auth_Server.security;


import com.form_builder.Auth_Server.client.UserClient;
import com.form_builder.Auth_Server.dto.CreateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoogleOAuthSuccessHandler {

    private final UserClient userClient;

    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication

    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email= oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        try{
            userClient.findByEmail(email);
        } catch( Exception e){
            CreateUserRequest req = new CreateUserRequest();
            req.setEmail(email);
            req.setUserName(name);

            userClient.createUser(req);

        }

        response.sendRedirect("login successfull");

    }

}
