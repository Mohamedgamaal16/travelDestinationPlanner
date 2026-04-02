package com.travelDestinationPlanner.fawry.mapper;

import com.travelDestinationPlanner.fawry.dto.UserWishlistResponseDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import com.travelDestinationPlanner.fawry.entity.User;
import com.travelDestinationPlanner.fawry.entity.UserWishlist;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-02T16:57:09+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25-loom (Oracle Corporation)"
)
@Component
public class UserWishlistMapperImpl implements UserWishlistMapper {

    @Autowired
    private DestinationMapper destinationMapper;

    @Override
    public UserWishlistResponseDto toResponseDto(UserWishlist userWishlist) {
        if ( userWishlist == null ) {
            return null;
        }

        UserWishlistResponseDto.UserWishlistResponseDtoBuilder userWishlistResponseDto = UserWishlistResponseDto.builder();

        userWishlistResponseDto.userId( userWishlistUserId( userWishlist ) );
        userWishlistResponseDto.destinationId( userWishlistDestinationId( userWishlist ) );
        userWishlistResponseDto.destination( destinationMapper.toResponseDto( userWishlist.getDestination() ) );
        userWishlistResponseDto.id( userWishlist.getId() );
        userWishlistResponseDto.addedAt( userWishlist.getAddedAt() );

        return userWishlistResponseDto.build();
    }

    private Long userWishlistUserId(UserWishlist userWishlist) {
        if ( userWishlist == null ) {
            return null;
        }
        User user = userWishlist.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long userWishlistDestinationId(UserWishlist userWishlist) {
        if ( userWishlist == null ) {
            return null;
        }
        Destination destination = userWishlist.getDestination();
        if ( destination == null ) {
            return null;
        }
        Long id = destination.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
