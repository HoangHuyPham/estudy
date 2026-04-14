package vn.nlu.huypham.app;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.nlu.huypham.app.constant.Roles;
import vn.nlu.huypham.app.entity.Role;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.repository.RoleRepo;
import vn.nlu.huypham.app.repository.UserRepo;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = getOrCreateRole(Roles.ADMIN);
        Role userRole = getOrCreateRole(Roles.STUDENT);
        Role tutorRole = getOrCreateRole(Roles.TUTOR);

        createUserIfNotExists(
                "Admin User",
                "admin",
                "admin@estudy.local",
                "admin123",
                adminRole);

        createUserIfNotExists(
                "Regular User",
                "student",
                "student@estudy.local",
                "student123",
                userRole);

        createUserIfNotExists(
                "Tutor User",
                "tutor",
                "tutor@estudy.local",
                "tutor123",
                tutorRole);
    }

    private Role getOrCreateRole(Roles name) {
        Role role = roleRepo.findByName(name).orElse(null);
        if (role != null) {
            return role;
        }

        return roleRepo.save(Role.builder()
                .name(name)
                .build());
    }

    private void createUserIfNotExists(String displayName, String username, String email, String rawPassword, Role role) {
        User existedUser = userRepo.findByUsername(username).orElse(null);
        if (existedUser != null) {
            return;
        }

        userRepo.save(User.builder()
                .displayName(displayName)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .isEnabled(true)
                .isAccountNonLocked(true)
                .roles(Set.of(role))
                .build());
    }
}
