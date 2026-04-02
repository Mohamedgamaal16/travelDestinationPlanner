package com.travelDestinationPlanner.fawry.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class DestinationRequestDto {

    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Country name must not exceed 100 characters")
    private String countryName;

    @Size(max = 100, message = "Capital must not exceed 100 characters")
    private String capital;

    @Size(max = 100, message = "Region must not exceed 100 characters")
    private String region;

    @Min(value = 0, message = "Population must be greater than or equal to 0")
    private Long population;

    @Size(max = 50, message = "Currency must not exceed 50 characters")
    private String currency;

    @Size(max = 10, message = "Currency symbol must not exceed 10 characters")
    private String currencySymbol;

    @Size(max = 500, message = "Flag URL must not exceed 500 characters")
    @Pattern(
            regexp = "^(https?://).+",
            message = "Flag URL must start with http:// or https://")
    private String flagUrl;

    private Boolean approved;


}
