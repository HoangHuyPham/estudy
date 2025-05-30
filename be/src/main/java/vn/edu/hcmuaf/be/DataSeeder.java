package vn.edu.hcmuaf.be;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import vn.edu.hcmuaf.be.entity.Role;
import vn.edu.hcmuaf.be.entity.User;
import vn.edu.hcmuaf.be.repository.RoleRepository;
import vn.edu.hcmuaf.be.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override

    public void run(String... args) {
        System.out.println("Data seeder phase");

        if (userRepository.count() == 0) {
            User userAdmin = User.builder().username("admin").password("123").build();
            Role roleAdmin = Role.builder().name("admin").build();

            userAdmin.addRole(roleAdmin);
            userRepository.save(userAdmin);


            Role r = roleRepository.findById(roleAdmin.getId()).orElse(null);
            System.out.println(r.getUsers());
        }

    }
}