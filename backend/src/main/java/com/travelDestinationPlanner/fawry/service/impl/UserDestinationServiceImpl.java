package com.travelDestinationPlanner.fawry.service.impl;

import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import com.travelDestinationPlanner.fawry.exception.TravelDestinationPlannerApiException;
import com.travelDestinationPlanner.fawry.mapper.DestinationMapper;
import com.travelDestinationPlanner.fawry.repository.DestinationRepository;
import com.travelDestinationPlanner.fawry.service.UserDestinationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDestinationServiceImpl implements UserDestinationService {

    private final DestinationRepository destinationRepository;
    private final DestinationMapper destinationMapper;


    @Override
    @Transactional(readOnly = true)
    public Page<DestinationResponseDto> getAllDestinations(Pageable pageable) {
        return destinationRepository.findByApprovedTrue(pageable).map(destinationMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DestinationResponseDto getDestinationById(Long id) {
        Destination destination = destinationRepository
                .findByIdAndApprovedTrue(id)
                .orElseThrow(() ->
                        new TravelDestinationPlannerApiException("Destination not found: " + id, HttpStatus.NOT_FOUND));
        return destinationMapper.toResponseDto(destination);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DestinationResponseDto> search(String name, Pageable pageable) {
        if (name == null || name.isBlank()) {
            return getAllDestinations(pageable);
        }
        return destinationRepository
                .findByApprovedTrueAndCountryNameContainingIgnoreCase(name.trim(), pageable)
                .map(destinationMapper::toResponseDto);
    }
}
