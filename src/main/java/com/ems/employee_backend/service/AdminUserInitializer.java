package com.ems.employee_backend.service;

import com.ems.employee_backend.entity.User;
import com.ems.employee_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setRole("ROLE_ADMIN");

                userRepository.save(admin);
                System.out.println("Default admin user created");
            }

//        if(userRepository.findByUsername("user").isEmpty()){
//            Users user = new Users();
//            user.setUsername("user");
//            user.setPassword(passwordEncoder.encode("user123"));
//            user.setRole(Role.USER);
//
//            userRepository.save(user);
//            System.out.println("Default admin user created");
//        }
        };

    }
}
