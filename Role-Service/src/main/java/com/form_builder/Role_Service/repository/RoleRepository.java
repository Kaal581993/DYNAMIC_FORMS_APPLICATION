package com.form_builder.Role_Service.repository;

import com.form_builder.Role_Service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
