package com.travelDestinationPlanner.fawry.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestCountryV3Dto {

    private NameDto name;
    private List<String> capital;
    private String region;
    private Long population;
    private Map<String, CurrencyDto> currencies;
    private FlagsDto flags;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NameDto {
        private String common;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrencyDto {
        private String name;
        private String symbol;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlagsDto {
        private String png;
    }
}
