package com.flyaway.travel.service;

import com.flyaway.travel.dto.LoginRequest;
import com.flyaway.travel.dto.LoginResponse;
import com.flyaway.travel.exception.BadRequestException;
import com.flyaway.travel.repository.UserRepository;
import com.flyaway.travel.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        if (userRepository.findByEmail(request.email()).isEmpty()) {
            throw new BadRequestException("Username does not exist");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            String token = tokenProvider.generateToken(auth);
            return new LoginResponse(token);
        } catch (Exception e) {
            throw new BadRequestException("Invalid credentials");
        }
    }
}