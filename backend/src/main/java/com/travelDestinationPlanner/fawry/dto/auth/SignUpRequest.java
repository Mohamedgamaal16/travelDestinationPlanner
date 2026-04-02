package com.travelDestinationPlanner.fawry.dto.auth;

import com.travelDestinationPlanner.fawry.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    private UserRole role;
}
