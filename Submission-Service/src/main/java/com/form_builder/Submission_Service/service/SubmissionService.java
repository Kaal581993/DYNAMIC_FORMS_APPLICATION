package com.form_builder.Submission_Service.service;

import com.form_builder.Submission_Service.dto.FieldDTO;
import com.form_builder.Submission_Service.dto.FieldPermissionResponse;
import com.form_builder.Submission_Service.dto.FormDTO;
import com.form_builder.Submission_Service.dto.FormPermissionResponse;
import com.form_builder.Submission_Service.dto.PermissionsByRoleRequest;
import com.form_builder.Submission_Service.dto.SubmissionResponse;
import com.form_builder.Submission_Service.dto.SubmitFormRequest;
import com.form_builder.Submission_Service.model.Submission;
import com.form_builder.Submission_Service.repository.FormClient;
import com.form_builder.Submission_Service.repository.RolePermissionClient;
import com.form_builder.Submission_Service.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

   private final SubmissionRepository submissionRepository;


   private final FormClient formClient;
   private final RolePermissionClient rolePermissionClient;
   private final SubmissionEmailService submissionEmailService;

    public  Submission submitForm(SubmitFormRequest request, String userId) {

        FormDTO formDTO = formClient.getForm(request.getFormId());

        Map<String, Object> normalizedData = normalizeSubmissionData(formDTO, request.getData());

        validateSubmissionPermissions(request.getFormId(), normalizedData);
        validateRequiredFields(formDTO, normalizedData);

        String submitterEmail = getCurrentUserEmail();

        Submission submission = Submission.builder()
                .formId(request.getFormId())
                .submittedBy(userId)
                .submittedByEmail(submitterEmail)
                .recipientEmail(request.getRecipientEmail())
                .submittedAt(LocalDateTime.now())
                .data(normalizedData)
                .build();

        Submission savedSubmission = submissionRepository.save(submission);
        submissionEmailService.sendSubmissionEmail(
                formDTO,
                savedSubmission,
                submitterEmail,
                request.getRecipientEmail()
        );
        return savedSubmission;


    }

    private Map<String, Object> normalizeSubmissionData(FormDTO formDTO, Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return Collections.emptyMap();
        }

        List<FieldDTO> fields = formDTO.getFields() == null ? List.of() : formDTO.getFields();

        Map<String, FieldDTO> fieldsById = fields.stream()
                .filter(field -> field.getId() != null && !field.getId().isBlank())
                .collect(Collectors.toMap(FieldDTO::getId, field -> field, (left, right) -> left));

        Map<String, List<FieldDTO>> fieldsByName = fields.stream()
                .filter(field -> field.getName() != null && !field.getName().isBlank())
                .collect(Collectors.groupingBy(field -> field.getName().toLowerCase(Locale.ROOT)));

        Map<String, Object> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            FieldDTO field = fieldsById.get(key);
            if (field == null) {
                List<FieldDTO> matches = fieldsByName.get(key.toLowerCase(Locale.ROOT));
                if (matches != null && !matches.isEmpty()) {
                    if (matches.size() > 1) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Field name is ambiguous: " + key);
                    }
                    field = matches.get(0);
                }
            }

            if (field == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field: " + key);
            }

            normalized.put(field.getId(), entry.getValue());
        }

        return normalized;
    }

    private void validateRequiredFields(FormDTO formDTO, Map<String, Object> data) {
        if (formDTO.getFields() == null || formDTO.getFields().isEmpty()) {
            return;
        }
        for (FieldDTO field : formDTO.getFields()) {
            if (field.isRequired() && !data.containsKey(field.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Required field missing: " + field.getId());
            }
        }
    }

    private void validateSubmissionPermissions(String formId, Map<String, Object> data) {
        List<String> roleNames = getCurrentRoles();
        if (roleNames.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No roles available for user");
        }

        PermissionsByRoleRequest request = new PermissionsByRoleRequest(formId, roleNames);
        List<FormPermissionResponse> formPermissions = rolePermissionClient.getFormPermissions(request);
        if (!formPermissions.isEmpty()
                && formPermissions.stream().noneMatch(permission -> Boolean.TRUE.equals(permission.getCanSubmit()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have submit permission");
        }

        List<FieldPermissionResponse> fieldPermissions = rolePermissionClient.getFieldPermissions(request);
        if (fieldPermissions.isEmpty()) {
            return;
        }

        Set<String> allowedFieldIds = fieldPermissions.stream()
                .filter(permission -> Boolean.TRUE.equals(permission.getEditable()))
                .map(FieldPermissionResponse::getFieldId)
                .collect(Collectors.toSet());

        for (String key : data.keySet()) {
            if (!allowedFieldIds.contains(key)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Field access denied: " + key);
            }
        }
    }

    private List<String> getCurrentRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return List.of();
        }
        return authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object details = authentication.getDetails();
        if (details instanceof String) {
            String email = ((String) details).trim();
            return email.isEmpty() ? null : email;
        }
        return null;
    }

    public  List<SubmissionResponse> getSubmissions(String formId) {

        validateViewPermissions(formId);
        List<SubmissionResponse> submissionResponse = submissionRepository.findByFormId(formId);

        return submissionResponse.stream().map(this::mapToResponse).collect(Collectors.toList());


}

    private void validateViewPermissions(String formId) {
        List<String> roleNames = getCurrentRoles();
        if (roleNames.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No roles available for user");
        }

        PermissionsByRoleRequest request = new PermissionsByRoleRequest(formId, roleNames);
        List<FormPermissionResponse> formPermissions = rolePermissionClient.getFormPermissions(request);
        if (!formPermissions.isEmpty()
                && formPermissions.stream().noneMatch(permission -> Boolean.TRUE.equals(permission.getCanView()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have view permission");
        }
    }

    private SubmissionResponse mapToResponse(SubmissionResponse submissionResponse) {

        SubmissionResponse response = new SubmissionResponse();
        response.setFormId(submissionResponse.getFormId());
        response.setData(submissionResponse.getData());
        response.setSubmittedAt(submissionResponse.getSubmittedAt());
        response.setSubmittedBy(submissionResponse.getSubmittedBy());

        return  response;

    }
    }
