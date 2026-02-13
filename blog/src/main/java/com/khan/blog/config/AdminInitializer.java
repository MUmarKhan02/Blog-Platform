package com.khan.blog.config;

import com.khan.blog.domain.entities.User;
import com.khan.blog.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//--------------------Initialize a Base Account/Admin--------------------\\
@Configuration
public class AdminInitializer {
    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(userRepository.findByEmail("admin@blog.com").isEmpty()){//Build the user
                User admin = User.builder()
                        .email("admin@blog.com")
                        .password(passwordEncoder.encode("Passw0rd123"))
                        .name("Blog Admin")
                        .build();
                userRepository.save(admin);

            }
        };
    }

}
