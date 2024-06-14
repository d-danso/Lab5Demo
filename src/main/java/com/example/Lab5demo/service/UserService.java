package com.example.Lab5demo.service;

import com.example.Lab5demo.dto.SignupRequest;
import com.example.Lab5demo.entity.Role;
import com.example.Lab5demo.entity.User;
import com.example.Lab5demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(SignupRequest request) {
        String username = request.username();
        Optional<User> existingUser = repository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new RuntimeException(String.format("User with the username '%s' already exists.", username));
        }

        String hashedPassword = encodePassword(request.password());
        Role role = request.role() != null ? request.role() : Role.USER; // Set default role to USER if not provided
        User user = new User(username, hashedPassword, role, request.name());
        repository.save(user);
    }
    public List<User> findUsers(){
        return repository.findAll();
    }
    public Optional<User> findUserById(Long id) {
        Optional<User> user = repository.findById(id);

        if (user.isPresent()) {
            return user;
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public User saveUser(User user) {
        return repository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<User> user = repository.findById(id);

        if (user.isPresent()) {
            repository.delete(user.get());
            System.out.println("User with id: {} deleted successfully");
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = repository.findByUsername(updatedUser.getUsername());
        if (existingUser.isPresent()){
            throw new RuntimeException("User  with username: " + updatedUser.getUsername() + "already exists");

        }
        return repository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            user.setName(updatedUser.getName());
            user.setRole(updatedUser.getRole());
            return saveUser(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
