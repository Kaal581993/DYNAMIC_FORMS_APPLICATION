package com.form_builder.Role_Service.controller;


import com.form_builder.Role_Service.dto.AssignFieldPermissionRequest;
import com.form_builder.Role_Service.dto.AssignFormPermissionRequest;
import com.form_builder.Role_Service.dto.CreateRoleRequest;
import com.form_builder.Role_Service.dto.FieldPermissionResponse;
import com.form_builder.Role_Service.dto.FormPermissionResponse;
import com.form_builder.Role_Service.dto.PermissionsByRoleRequest;
import com.form_builder.Role_Service.model.Role;
import com.form_builder.Role_Service.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(
            @Valid @RequestBody CreateRoleRequest request
    ){
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PostMapping("/{roleId}/assignFormPermissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignFormPermissions(
            @PathVariable UUID roleId, @RequestBody AssignFormPermissionRequest request
            ){

        roleService.assignFormPermission(roleId, request);

        return ResponseEntity.ok().build();

    }


    @PostMapping("/{roleId}/assignFieldPermissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignFieldPermissions(
            @PathVariable UUID roleId, @RequestBody AssignFieldPermissionRequest request
    ){

        roleService.assignFieldPermissions(roleId, request);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String roleName) {
        return ResponseEntity.ok(roleService.getRoleByName(roleName));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @PostMapping("/permissions/forms")
    public ResponseEntity<List<FormPermissionResponse>> getFormPermissions(
            @RequestBody PermissionsByRoleRequest request) {
        return ResponseEntity.ok(roleService.getFormPermissions(request.getFormId(), request.getRoleNames()));
    }

    @PostMapping("/permissions/fields")
    public ResponseEntity<List<FieldPermissionResponse>> getFieldPermissions(
            @RequestBody PermissionsByRoleRequest request) {
        return ResponseEntity.ok(roleService.getFieldPermissions(request.getFormId(), request.getRoleNames()));
    }
}
