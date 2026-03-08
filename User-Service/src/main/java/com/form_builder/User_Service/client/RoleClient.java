package com.form_builder.User_Service.client;


import com.form_builder.User_Service.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="role-service")
public interface RoleClient {

    @GetMapping("/api/roles/{roleId}")
    RoleDTO getRole(@PathVariable UUID roleId);
}
