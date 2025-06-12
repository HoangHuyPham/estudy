package vn.edu.hcmuaf.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import vn.edu.hcmuaf.be.entity.Role;
import vn.edu.hcmuaf.be.entity.User;
import vn.edu.hcmuaf.be.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("Data seeder phase");

        if (userRepository.count() == 0) {
            User userAdmin = User.builder().username("admin").password(BCrypt.hashpw("123", BCrypt.gensalt())).build();
            Role roleAdmin = Role.builder().name("admin").build();

            userAdmin.addRole(roleAdmin);
            userRepository.save(userAdmin);
        }
    }
}