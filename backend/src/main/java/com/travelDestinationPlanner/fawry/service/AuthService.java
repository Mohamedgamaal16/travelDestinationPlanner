package com.travelDestinationPlanner.fawry.service;

import com.travelDestinationPlanner.fawry.dto.auth.AuthResponse;
import com.travelDestinationPlanner.fawry.dto.auth.LoginRequest;
import com.travelDestinationPlanner.fawry.dto.auth.RefreshRequest;
import com.travelDestinationPlanner.fawry.dto.auth.SignUpRequest;

public interface AuthService {
    AuthResponse signUp(SignUpRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshRequest request);
}
