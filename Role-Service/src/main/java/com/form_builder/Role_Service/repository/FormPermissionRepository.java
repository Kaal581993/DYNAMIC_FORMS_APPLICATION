package com.form_builder.Role_Service.repository;

import com.form_builder.Role_Service.model.FormPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormPermissionRepository extends JpaRepository<FormPermission, UUID> {
    Optional<FormPermission> findByRoleIdAndFormId(UUID roleId, String formId);

    List<FormPermission> findByRole_NameInAndFormId(List<String> roleNames, String formId);
}
