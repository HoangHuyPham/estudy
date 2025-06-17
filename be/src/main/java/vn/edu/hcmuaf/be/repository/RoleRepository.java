package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.be.entity.Role;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
