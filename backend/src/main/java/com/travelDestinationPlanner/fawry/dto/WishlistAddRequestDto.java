package com.travelDestinationPlanner.fawry.dto;

import jakarta.validation.constraints.NotNull;
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
public class WishlistAddRequestDto {

    @NotNull(message = "Destination id is required")
    private Long destinationId;
}
