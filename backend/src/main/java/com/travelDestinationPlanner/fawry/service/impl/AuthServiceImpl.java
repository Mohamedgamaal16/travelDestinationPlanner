package com.travelDestinationPlanner.fawry.service.impl;

import com.travelDestinationPlanner.fawry.dto.auth.AuthResponse;
import com.travelDestinationPlanner.fawry.dto.auth.LoginRequest;
import com.travelDestinationPlanner.fawry.dto.auth.RefreshRequest;
import com.travelDestinationPlanner.fawry.dto.auth.SignUpRequest;
import com.travelDestinationPlanner.fawry.entity.User;
import com.travelDestinationPlanner.fawry.enums.UserRole;
import com.travelDestinationPlanner.fawry.exception.TravelDestinationPlannerApiException;
import com.travelDestinationPlanner.fawry.repository.UserRepository;
import com.travelDestinationPlanner.fawry.security.JwtUtil;
import com.travelDestinationPlanner.fawry.security.UserDetailsServiceImpl;
import com.travelDestinationPlanner.fawry.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public AuthResponse signUp(SignUpRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new TravelDestinationPlannerApiException("Email already exists.");
        });
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new TravelDestinationPlannerApiException("this user name already exists.");
        });

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() == null ? UserRole.USER : request.getRole())
                .enabled(true)
                .build();

        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return buildAuthResponse(userDetails);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        return buildAuthResponse(userDetails);
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        String email = jwtUtil.extractUserName(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            throw new TravelDestinationPlannerApiException("Invalid refresh token.");
        }
        return AuthResponse.builder()
                .accessToken(jwtUtil.generateToken(email))
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInSeconds(1800L)
                .build();
    }

    private AuthResponse buildAuthResponse(UserDetails userDetails) {
        return AuthResponse.builder()
                .accessToken(jwtUtil.generateToken(userDetails.getUsername()))
                .refreshToken(jwtUtil.generateRefreshToken(userDetails))
                .tokenType("Bearer")
                .expiresInSeconds(1800L)
                .build();
    }
}
