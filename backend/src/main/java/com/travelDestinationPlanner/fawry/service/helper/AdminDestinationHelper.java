package com.travelDestinationPlanner.fawry.service.helper;

import com.travelDestinationPlanner.fawry.client.dto.RestCountryV3Dto;
import com.travelDestinationPlanner.fawry.dto.DestinationRequestDto;
import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import java.time.LocalDateTime;
import java.util.Map;

public final class AdminDestinationHelper {

    private AdminDestinationHelper() {}

    // public static void persistTimestamps(Destination destination) {
    //     LocalDateTime now = LocalDateTime.now();
    //     destination.setCreatedAt(now);
    //     destination.setUpdatedAt(now);
    // }

//    public static void applyAdminDefaults(Destination destination, DestinationRequestDto dto) {
//        if (dto.getApproved() != null) {
//            destination.setApproved(dto.getApproved());
//        } else {
//            destination.setApproved(Boolean.TRUE);
//        }
//    }

    public static boolean hasCommonName(RestCountryV3Dto country) {
        return country.getName() != null
                && country.getName().getCommon() != null
                && !country.getName().getCommon().isBlank();
    }

    public static DestinationResponseDto toSuggestionDto(RestCountryV3Dto country) {
        String capital = null;
        if (country.getCapital() != null && !country.getCapital().isEmpty()) {
            capital = truncate(country.getCapital().get(0), 100);
        }

        String currencyLabel = null;
        String currencySymbol = null;
        Map<String, RestCountryV3Dto.CurrencyDto> currencies = country.getCurrencies();
        if (currencies != null && !currencies.isEmpty()) {
            Map.Entry<String, RestCountryV3Dto.CurrencyDto> first =
                    currencies.entrySet().iterator().next();
            RestCountryV3Dto.CurrencyDto info = first.getValue();
            if (info != null && info.getName() != null) {
                currencyLabel = truncate(info.getName(), 50);
            } else {
                currencyLabel = truncate(first.getKey(), 50);
            }
            if (info != null) {
                currencySymbol = truncate(info.getSymbol(), 10);
            }
        }

        String countryName = null;
        if (country.getName() != null) {
            countryName = truncate(country.getName().getCommon(), 100);
        }

        String flagUrl = null;
        if (country.getFlags() != null) {
            flagUrl = truncate(country.getFlags().getPng(), 500);
        }

        return DestinationResponseDto.builder()
                .id(null)
                .countryName(countryName)
                .capital(capital)
                .region(truncate(country.getRegion(), 100))
                .population(country.getPopulation())
                .currency(currencyLabel)
                .currencySymbol(currencySymbol)
                .flagUrl(flagUrl)
                .build();
    }

    private static String truncate(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }
}
