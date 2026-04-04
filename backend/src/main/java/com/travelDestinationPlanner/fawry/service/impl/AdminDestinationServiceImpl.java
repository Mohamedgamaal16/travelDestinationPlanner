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
import com.travelDestinationPlanner.fawry.service.helper.AdminDestinationHelper;
import feign.FeignException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
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
                    .filter(AdminDestinationHelper::hasCommonName)
                    .map(AdminDestinationHelper::toSuggestionDto)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (FeignException e) {
            throw new TravelDestinationPlannerApiException("Failed to fetch countries: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public DestinationResponseDto addDestination(DestinationRequestDto dto) {
        Destination destination = destinationMapper.toEntity(dto);
//        AdminDestinationHelper.applyAdminDefaults(destination, dto);
        // AdminDestinationHelper.persistTimestamps(destination);
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
    public DestinationResponseDto approveDestination(Long id) {
        Destination destination = destinationRepository
                .findById(id)
                .orElseThrow(() ->
                        new TravelDestinationPlannerApiException("Destination not found: " + id, HttpStatus.NOT_FOUND));
        destination.setApproved(true);
        destination.setUpdatedAt(LocalDateTime.now());
        return destinationMapper.toResponseDto(destinationRepository.save(destination));
    }

    @Override
    @Transactional
    public DestinationResponseDto disapproveDestination(Long id) {
        Destination destination = destinationRepository
                .findById(id)
                .orElseThrow(() ->
                        new TravelDestinationPlannerApiException("Destination not found: " + id, HttpStatus.NOT_FOUND));
        destination.setApproved(false);
        destination.setUpdatedAt(LocalDateTime.now());
        return destinationMapper.toResponseDto(destinationRepository.save(destination));
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
//            AdminDestinationHelper.applyAdminDefaults(destination, dto);
            destination.setCreatedAt(now);
            destination.setUpdatedAt(now);
            results.add(destinationMapper.toResponseDto(destinationRepository.save(destination)));
        }
        return results;
    }
}
