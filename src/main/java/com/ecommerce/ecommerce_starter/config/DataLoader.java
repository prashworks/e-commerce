package com.ecommerce.ecommerce_starter.config;

import com.ecommerce.ecommerce_starter.model.Role;
import com.ecommerce.ecommerce_starter.model.User;
import com.ecommerce.ecommerce_starter.repository.RoleRepository;
import com.ecommerce.ecommerce_starter.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public DataLoader(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {

        // Create roles if not exist
        Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_ADMIN");
                    return roleRepo.save(r);
                });

        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    return roleRepo.save(r);
                });

        // Create default admin user if not exist
        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));
            userRepo.save(admin);
            System.out.println("✅ Admin user created: admin / admin123");
        }
    }
}