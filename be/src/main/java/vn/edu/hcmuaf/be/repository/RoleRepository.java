package vn.edu.hcmuaf.be.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.Role;

public interface RoleRepository extends JpaRepository<Role, UUID>{}
