package com.form_builder.Role_Service.dto;

import com.form_builder.Role_Service.model.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignFormPermissionRequest {

    private String formId;

    private Boolean canView;
    private Boolean canSubmit;
    private Boolean canEditSubmission;

}
