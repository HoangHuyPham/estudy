package vn.nlu.huypham.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.nlu.huypham.app.constant.Roles;
import vn.nlu.huypham.app.entity.Role;

public interface RoleRepo extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Roles name);
}
