package com.form_builder.Role_Service.controller;


import com.form_builder.Role_Service.dto.AssignFieldPermissionRequest;
import com.form_builder.Role_Service.dto.AssignFormPermissionRequest;
import com.form_builder.Role_Service.dto.CreateRoleRequest;
import com.form_builder.Role_Service.model.Role;
import com.form_builder.Role_Service.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ){
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PostMapping("/{roleId}/assignFormPermissions")
    public ResponseEntity<Void> assignFormPermissions(
            @PathVariable UUID roleId, @RequestBody AssignFormPermissionRequest request
            ){

        roleService.assignFormPermission(roleId, request);

        return ResponseEntity.ok().build();

    }


    @PostMapping("/{roleId}/assignFieldPermissions")
    public ResponseEntity<Void> assignFieldPermissions(
            @PathVariable UUID roleId, @RequestBody AssignFieldPermissionRequest request
    ){

        roleService.assignFieldPermissions(roleId, request);

        return ResponseEntity.ok().build();

    }
}
