package com.form_builder.Role_Service.service;

import com.form_builder.Role_Service.dto.AssignFieldPermissionRequest;
import com.form_builder.Role_Service.dto.AssignFormPermissionRequest;
import com.form_builder.Role_Service.dto.CreateRoleRequest;
import com.form_builder.Role_Service.model.FieldPermission;
import com.form_builder.Role_Service.model.FormPermission;
import com.form_builder.Role_Service.model.Role;
import com.form_builder.Role_Service.repository.FieldPermissionRepository;
import com.form_builder.Role_Service.repository.FormPermissionRepository;
import com.form_builder.Role_Service.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final FormPermissionRepository formPermissionRepository;
    private final FieldPermissionRepository fieldPermissionRepository;


    public  Role createRole(@Valid CreateRoleRequest request) {

        if (roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role with name '" + request.getName() + "' already exists");
        }

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return roleRepository.save(role);


    }

    public void assignFormPermission(UUID roleId, AssignFormPermissionRequest request) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new RuntimeException("Role not found"));

        FormPermission formPermission = FormPermission.builder()
                .formId(request.getFormId())
                .canView(request.getCanView())
                .canSubmit(request.getCanSubmit())
                .canEditSubmission(request.getCanEditSubmission())
                .build();

        formPermissionRepository.save(formPermission);

    }

    public void assignFieldPermissions(UUID roleId, AssignFieldPermissionRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new RuntimeException("Role not found"));

        FieldPermission fieldPermission = FieldPermission.builder()
                .formId(request.getFormId())
                .fieldId(request.getFieldId())
                .visible(request.getVisible())
                .editable(request.getEditable())
                .editable(request.getEditable())
                .requiredOverride(request.getRequiredOverride())
                .masked(request.getMasked())
                .role(role)
                .build();

        fieldPermissionRepository.save(fieldPermission);
    }
}
