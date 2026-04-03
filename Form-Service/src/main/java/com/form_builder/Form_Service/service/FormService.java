package com.form_builder.Form_Service.service;


import com.form_builder.Form_Service.client.RoleClient;
import com.form_builder.Form_Service.dto.AddFieldRequest;
import com.form_builder.Form_Service.dto.AssignFieldPermissionRequest;
import com.form_builder.Form_Service.dto.AssignFormPermissionRequest;
import com.form_builder.Form_Service.dto.CreateFormRequest;
import com.form_builder.Form_Service.dto.FieldRolePermissionRequest;
import com.form_builder.Form_Service.dto.FormRolePermissionRequest;
import com.form_builder.Form_Service.dto.RoleDTO;
import com.form_builder.Form_Service.model.FieldDefinition;
import com.form_builder.Form_Service.model.Form;
import com.form_builder.Form_Service.repository.FormRepository;
import jakarta.validation.Valid;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FormService {

    private final FormRepository formRepository;
    private final RoleClient roleClient;

    public FormService(FormRepository formRepository, RoleClient roleClient) {
        this.formRepository = formRepository;
        this.roleClient = roleClient;
    }

    public  Form createForm(@Valid CreateFormRequest request) {
        Form form = new Form();

        form.setName(request.getName());
        form.setDescription(request.getDescription());
        if (request.getOwnerEmail() != null && !request.getOwnerEmail().isBlank()) {
            form.setOwnerEmail(request.getOwnerEmail().trim().toLowerCase());
        }
        form.setFields(new ArrayList<>());
        form.setVersion(1);
        form.setActive(true);
        form.setCreatedAt(LocalDateTime.now());

        Form savedForm = formRepository.save(form);
        assignFormPermissions(savedForm.getId(), request.getRolePermissions());
        return savedForm;
    }

    public Form addField(String formId, @Valid AddFieldRequest request) {

        Form form = formRepository.findById(formId).orElseThrow(
                ()-> new RuntimeException("Form not found")
        );

        String fieldId = UUID.randomUUID().toString();
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setId(fieldId);
        fieldDefinition.setLabel(request.getLabel());
        fieldDefinition.setName(request.getName());
        fieldDefinition.setConfig(request.getConfig());
        fieldDefinition.setRequired(request.isRequired());
        fieldDefinition.setType(request.getType());

        form.getFields().add(fieldDefinition);
        Form savedForm = formRepository.save(form);
        assignFieldPermissions(formId, fieldId, request.getRolePermissions());
        return savedForm;

    }

    public Form getForm(String formId) {
        return formRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("Form not found"));
    }

    private void assignFormPermissions(String formId, List<FormRolePermissionRequest> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        for (FormRolePermissionRequest permission : permissions) {
            if (permission == null || permission.getRoleName() == null || permission.getRoleName().isBlank()) {
                continue;
            }
            RoleDTO role = roleClient.getRoleByName(permission.getRoleName());
            AssignFormPermissionRequest request = new AssignFormPermissionRequest(
                    formId,
                    permission.getCanView(),
                    permission.getCanSubmit(),
                    permission.getCanEditSubmission()
            );
            roleClient.assignFormPermissions(role.getId(), request);
        }
    }

    private void assignFieldPermissions(String formId, String fieldId, List<FieldRolePermissionRequest> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }
        for (FieldRolePermissionRequest permission : permissions) {
            if (permission == null || permission.getRoleName() == null || permission.getRoleName().isBlank()) {
                continue;
            }
            RoleDTO role = roleClient.getRoleByName(permission.getRoleName());
            AssignFieldPermissionRequest request = new AssignFieldPermissionRequest(
                    formId,
                    fieldId,
                    permission.getVisible(),
                    permission.getEditable(),
                    permission.getRequiredOverride(),
                    permission.getMasked()
            );
            roleClient.assignFieldPermissions(role.getId(), request);
        }
    }
}
