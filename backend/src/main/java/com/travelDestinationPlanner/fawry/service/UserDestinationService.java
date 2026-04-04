package com.travelDestinationPlanner.fawry.service;

import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDestinationService {

    Page<DestinationResponseDto> getAllDestinations(Pageable pageable);

    DestinationResponseDto getDestinationById(Long id);

    Page<DestinationResponseDto> search(String name, Pageable pageable, Boolean approved);
}
