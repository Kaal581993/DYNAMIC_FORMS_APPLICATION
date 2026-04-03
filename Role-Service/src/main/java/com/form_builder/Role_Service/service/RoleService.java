package com.form_builder.Role_Service.service;

import com.form_builder.Role_Service.dto.AssignFieldPermissionRequest;
import com.form_builder.Role_Service.dto.AssignFormPermissionRequest;
import com.form_builder.Role_Service.dto.CreateRoleRequest;
import com.form_builder.Role_Service.dto.FieldPermissionResponse;
import com.form_builder.Role_Service.dto.FormPermissionResponse;
import com.form_builder.Role_Service.model.FieldPermission;
import com.form_builder.Role_Service.model.FormPermission;
import com.form_builder.Role_Service.model.Role;
import com.form_builder.Role_Service.repository.FieldPermissionRepository;
import com.form_builder.Role_Service.repository.FormPermissionRepository;
import com.form_builder.Role_Service.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


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

        FormPermission formPermission = formPermissionRepository
                .findByRoleIdAndFormId(roleId, request.getFormId())
                .orElseGet(FormPermission::new);

        formPermission.setRole(role);
        formPermission.setFormId(request.getFormId());
        formPermission.setCanView(request.getCanView());
        formPermission.setCanSubmit(request.getCanSubmit());
        formPermission.setCanEditSubmission(request.getCanEditSubmission());

        formPermissionRepository.save(formPermission);

    }

    public void assignFieldPermissions(UUID roleId, AssignFieldPermissionRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new RuntimeException("Role not found"));

        FieldPermission fieldPermission = fieldPermissionRepository
                .findByRoleIdAndFormIdAndFieldId(roleId, request.getFormId(), request.getFieldId())
                .orElseGet(FieldPermission::new);

        fieldPermission.setRole(role);
        fieldPermission.setFormId(request.getFormId());
        fieldPermission.setFieldId(request.getFieldId());
        fieldPermission.setVisible(request.getVisible());
        fieldPermission.setEditable(request.getEditable());
        fieldPermission.setRequiredOverride(request.getRequiredOverride());
        fieldPermission.setMasked(request.getMasked());

        fieldPermissionRepository.save(fieldPermission);
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    public Role getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
    }

    public List<FormPermissionResponse> getFormPermissions(String formId, List<String> roleNames) {
        List<String> normalizedNames = normalizeRoleNames(roleNames);
        return formPermissionRepository.findByRole_NameInAndFormId(normalizedNames, formId)
                .stream()
                .map(permission -> new FormPermissionResponse(
                        permission.getFormId(),
                        permission.getRole().getName(),
                        permission.getCanView(),
                        permission.getCanSubmit(),
                        permission.getCanEditSubmission()
                ))
                .collect(Collectors.toList());
    }

    public List<FieldPermissionResponse> getFieldPermissions(String formId, List<String> roleNames) {
        List<String> normalizedNames = normalizeRoleNames(roleNames);
        return fieldPermissionRepository.findByRole_NameInAndFormId(normalizedNames, formId)
                .stream()
                .map(permission -> new FieldPermissionResponse(
                        permission.getFormId(),
                        permission.getFieldId(),
                        permission.getRole().getName(),
                        permission.getVisible(),
                        permission.getEditable(),
                        permission.getRequiredOverride(),
                        permission.getMasked()
                ))
                .collect(Collectors.toList());
    }

    private List<String> normalizeRoleNames(List<String> roleNames) {
        if (roleNames == null) {
            return List.of();
        }
        return roleNames.stream()
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .map(name -> {
                    String upper = name.toUpperCase(Locale.ROOT);
                    if (upper.startsWith("ROLE_")) {
                        return upper.substring(5);
                    }
                    return upper;
                })
                .distinct()
                .toList();
    }
}
