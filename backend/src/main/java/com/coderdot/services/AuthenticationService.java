package com.coderdot.services;


import com.coderdot.dto.LoginUserDto;
import com.coderdot.dto.RegisterUserDto;
import com.coderdot.models.Role;
import com.coderdot.models.User;
import com.coderdot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRoles(new HashSet<>(Collections.singletonList(Role.USER)));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public void initUserRole() {
        String adminEmail = "admin@email.com";

        // Check if user with admin email already exists
        Optional<User> existingUser = userRepository.findByEmail(adminEmail);

        if (existingUser.isEmpty()) {
            User user = new User();
            user.setEmail(adminEmail);
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRoles(new HashSet<>(Collections.singletonList(Role.ADMIN)));
            user.setFullName("Admin User");
            userRepository.save(user);
        } else {
            // User with admin email already exists, you can choose to update or log an error
            // For simplicity, let's update the existing user's password
            User existingAdmin = existingUser.get();
            existingAdmin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(existingAdmin);
        }
    }

}
