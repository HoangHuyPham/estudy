package vn.nlu.huypham.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.nlu.huypham.app.entity.User;



public interface UserRepo extends JpaRepository<User, UUID>{
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsernameOrEmail(String user, String email);
}
