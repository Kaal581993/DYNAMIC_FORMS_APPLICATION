package com.form_builder.Role_Service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "field_permission")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String formId;
    private String fieldId;

    private Boolean visible;
    private Boolean editable;
    private Boolean requiredOverride;
    private Boolean masked;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
