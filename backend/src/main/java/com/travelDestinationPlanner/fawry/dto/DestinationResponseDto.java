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
public class DestinationResponseDto {

    private Long id;
    private String countryName;
    private String capital;
    private String region;
    private Long population;
    private String currency;
    private String currencySymbol;
    private String flagUrl;

}
