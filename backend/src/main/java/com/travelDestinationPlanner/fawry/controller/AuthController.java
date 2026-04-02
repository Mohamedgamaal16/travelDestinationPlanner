package com.travelDestinationPlanner.fawry.controller;

import com.travelDestinationPlanner.fawry.dto.auth.AuthResponse;
import com.travelDestinationPlanner.fawry.dto.auth.LoginRequest;
import com.travelDestinationPlanner.fawry.dto.auth.RefreshRequest;
import com.travelDestinationPlanner.fawry.dto.auth.SignUpRequest;
import com.travelDestinationPlanner.fawry.enums.UserRole;
import com.travelDestinationPlanner.fawry.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        request.setRole(UserRole.USER);

        return ResponseEntity.ok(authService.signUp(request));
    }
    @PostMapping("admin/sign-up")
    public ResponseEntity<AuthResponse> signUpAdmin(@Valid @RequestBody SignUpRequest request) {
        request.setRole(UserRole.ADMIN);
        return ResponseEntity.ok(authService.signUp(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
