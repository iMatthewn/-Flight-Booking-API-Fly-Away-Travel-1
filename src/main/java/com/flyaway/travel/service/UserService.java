package com.flyaway.travel.service;

import com.flyaway.travel.dto.UserRegisterRequest;
import com.flyaway.travel.dto.UserResponse;
import com.flyaway.travel.exception.BadRequestException;
import com.flyaway.travel.model.User;
import com.flyaway.travel.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.USER)
                .build();

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Username does not exist"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}