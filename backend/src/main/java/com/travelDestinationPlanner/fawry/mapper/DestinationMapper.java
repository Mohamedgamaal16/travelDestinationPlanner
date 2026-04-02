package com.travelDestinationPlanner.fawry.mapper;

import com.travelDestinationPlanner.fawry.dto.DestinationRequestDto;
import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DestinationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Destination toEntity(DestinationRequestDto requestDto);

    DestinationResponseDto toResponseDto(Destination destination);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(DestinationRequestDto requestDto, @MappingTarget Destination destination);
}
