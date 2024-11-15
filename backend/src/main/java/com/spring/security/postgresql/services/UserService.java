package com.spring.security.postgresql.services;

import com.spring.security.postgresql.exception.ResourceNotFoundException;
import com.spring.security.postgresql.models.Role;
import com.spring.security.postgresql.models.User;
import com.spring.security.postgresql.repository.RoleRepository;
import com.spring.security.postgresql.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }


    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findByName(updatedUser.getRoles().stream().findFirst().get().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found§"));
        Set<Role> roles = new HashSet<>(Collections.singleton(role));
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRoles(roles);
        return userRepository.save(existingUser);
    }

    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    public void deleteUser(Long userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        userRepository.delete(userToDelete);
    }

    public User fetchCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with name: " + currentUserName));
    }
}
