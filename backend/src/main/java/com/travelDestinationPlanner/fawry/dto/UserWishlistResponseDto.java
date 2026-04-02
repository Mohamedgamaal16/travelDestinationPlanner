package com.travelDestinationPlanner.fawry.dto;

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
public class UserWishlistResponseDto {

    private Long id;
    private Long userId;
    private Long destinationId;
    private DestinationResponseDto destination;
    private LocalDateTime addedAt;
}
