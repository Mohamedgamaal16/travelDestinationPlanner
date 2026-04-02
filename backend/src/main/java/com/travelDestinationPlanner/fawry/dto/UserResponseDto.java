package com.travelDestinationPlanner.fawry.dto;

import com.travelDestinationPlanner.fawry.enums.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
