package com.form_builder.Role_Service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "form_permission")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String formId;

    private Boolean canView;
    private Boolean canSubmit;
    private Boolean canEditSubmission;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
