package com.travelDestinationPlanner.fawry.service.impl;

import com.travelDestinationPlanner.fawry.client.RestCountriesClient;
import com.travelDestinationPlanner.fawry.client.dto.RestCountryV3Dto;
import com.travelDestinationPlanner.fawry.dto.DestinationRequestDto;
import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import com.travelDestinationPlanner.fawry.exception.TravelDestinationPlannerApiException;
import com.travelDestinationPlanner.fawry.mapper.DestinationMapper;
import com.travelDestinationPlanner.fawry.repository.DestinationRepository;
import com.travelDestinationPlanner.fawry.service.AdminDestinationService;
import feign.FeignException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDestinationServiceImpl implements AdminDestinationService {

    private static final String REST_COUNTRIES_FIELDS = "name,capital,region,population,currencies,flags";

    private final RestCountriesClient restCountriesClient;
    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DestinationResponseDto> getSuggestions() {
        try {
            List<RestCountryV3Dto> countries = restCountriesClient.getAllCountries(REST_COUNTRIES_FIELDS);
            if (countries == null) {
                return List.of();
            }
            return countries.stream()
                    .filter(this::hasCommonName)
                    .map(this::toSuggestionDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (FeignException e) {
            throw new TravelDestinationPlannerApiException("Failed to fetch countries: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DestinationResponseDto addDestination(DestinationRequestDto dto) {
        Destination destination = destinationMapper.toEntity(dto);
        applyAdminDefaults(destination, dto);
        persistTimestamps(destination);
        Destination saved = destinationRepository.save(destination);
        return destinationMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void deleteDestination(Long id) {
        Destination destination = destinationRepository
                .findById(id)
                .orElseThrow(() -> new TravelDestinationPlannerApiException("Destination not found: " + id));
        destinationRepository.delete(destination);
    }

    @Override
    @Transactional
    public List<DestinationResponseDto> bulkAdd(List<DestinationRequestDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }
        List<DestinationResponseDto> results = new ArrayList<>(dtos.size());
        LocalDateTime now = LocalDateTime.now();
        for (DestinationRequestDto dto : dtos) {
            Destination destination = destinationMapper.toEntity(dto);
            applyAdminDefaults(destination, dto);
            destination.setCreatedAt(now);
            destination.setUpdatedAt(now);
            results.add(destinationMapper.toResponseDto(destinationRepository.save(destination)));
        }
        return results;
    }

    private void persistTimestamps(Destination destination) {
        LocalDateTime now = LocalDateTime.now();
        destination.setCreatedAt(now);
        destination.setUpdatedAt(now);
    }

    private void applyAdminDefaults(Destination destination, DestinationRequestDto dto) {
        if (dto.getApproved() != null) {
            destination.setApproved(dto.getApproved());
        } else {
            destination.setApproved(Boolean.TRUE);
        }
    }

    private boolean hasCommonName(RestCountryV3Dto country) {
        return country.getName() != null
                && country.getName().getCommon() != null
                && !country.getName().getCommon().isBlank();
    }

    private DestinationResponseDto toSuggestionDto(RestCountryV3Dto country) {
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
