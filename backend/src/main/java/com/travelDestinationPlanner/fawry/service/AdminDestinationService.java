package com.travelDestinationPlanner.fawry.service;

import com.travelDestinationPlanner.fawry.dto.DestinationRequestDto;
import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import java.util.List;

public interface AdminDestinationService {

    List<DestinationResponseDto> getSuggestions();

    DestinationResponseDto addDestination(DestinationRequestDto dto);

    void deleteDestination(Long id);

    List<DestinationResponseDto> bulkAdd(List<DestinationRequestDto> dtos);
}
