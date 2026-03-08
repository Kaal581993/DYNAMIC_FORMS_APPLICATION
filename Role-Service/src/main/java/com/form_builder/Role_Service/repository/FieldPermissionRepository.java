package com.form_builder.Role_Service.repository;

import com.form_builder.Role_Service.model.FieldPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldPermissionRepository extends JpaRepository<FieldPermission, UUID> {
}
