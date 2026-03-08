package com.form_builder.Role_Service.repository;

import com.form_builder.Role_Service.model.FormPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormPermissionRepository extends JpaRepository<FormPermission, UUID> {
}
