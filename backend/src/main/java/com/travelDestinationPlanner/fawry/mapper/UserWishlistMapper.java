package com.travelDestinationPlanner.fawry.mapper;

import com.travelDestinationPlanner.fawry.dto.UserWishlistResponseDto;
import com.travelDestinationPlanner.fawry.entity.UserWishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DestinationMapper.class)
public interface UserWishlistMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "destinationId", source = "destination.id")
    @Mapping(target = "destination", source = "destination")
    UserWishlistResponseDto toResponseDto(UserWishlist userWishlist);
}
