package com.travelDestinationPlanner.fawry.mapper;

import com.travelDestinationPlanner.fawry.dto.UserRequestDto;
import com.travelDestinationPlanner.fawry.dto.UserResponseDto;
import com.travelDestinationPlanner.fawry.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-02T16:57:09+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25-loom (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( requestDto.getUsername() );
        user.password( requestDto.getPassword() );
        user.email( requestDto.getEmail() );
        user.role( requestDto.getRole() );
        user.enabled( requestDto.getEnabled() );

        return user.build();
    }

    @Override
    public UserResponseDto toResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto.UserResponseDtoBuilder userResponseDto = UserResponseDto.builder();

        userResponseDto.id( user.getId() );
        userResponseDto.username( user.getUsername() );
        userResponseDto.email( user.getEmail() );
        userResponseDto.role( user.getRole() );
        userResponseDto.enabled( user.getEnabled() );
        userResponseDto.createdAt( user.getCreatedAt() );

        return userResponseDto.build();
    }

    @Override
    public void updateEntityFromRequest(UserRequestDto requestDto, User user) {
        if ( requestDto == null ) {
            return;
        }

        user.setUsername( requestDto.getUsername() );
        user.setPassword( requestDto.getPassword() );
        user.setEmail( requestDto.getEmail() );
        user.setRole( requestDto.getRole() );
        user.setEnabled( requestDto.getEnabled() );
    }
}
