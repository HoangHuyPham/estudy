package vn.nlu.huypham.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.nlu.huypham.app.entity.User;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);  
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :input OR u.email = :input")
    Optional<User> findByUsernameOrEmail(@Param("input") String input);
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    boolean existsByUsernameOrEmail(String user, String email);
}
