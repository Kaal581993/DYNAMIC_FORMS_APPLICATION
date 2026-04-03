package com.form_builder.Role_Service.repository;

import com.form_builder.Role_Service.model.FieldPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FieldPermissionRepository extends JpaRepository<FieldPermission, UUID> {
    Optional<FieldPermission> findByRoleIdAndFormIdAndFieldId(UUID roleId, String formId, String fieldId);

    List<FieldPermission> findByRole_NameInAndFormId(List<String> roleNames, String formId);
}
