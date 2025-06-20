package com.genx.config;

import com.genx.enums.AuthProvider;
import com.genx.entity.User;
import com.genx.enums.ERole;
import com.genx.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AdminAccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String username = "admin";
        String email = "admin@example.com";

        if (!userRepository.existsByUsername(username)) {
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(ERole.ADMIN);
            admin.setFullName("DNA-GenX");
            admin.setEnabled(true);
            admin.setAccountNonLocked(true);
            admin.setAuthProvider(AuthProvider.LOCAL);


            userRepository.save(admin);
            System.out.println("✅ Admin account created.");
        } else {
            System.out.println("ℹ️ Admin already exists.");
        }
    }
}