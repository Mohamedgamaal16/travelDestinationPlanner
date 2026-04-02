package com.travelDestinationPlanner.fawry.mapper;

import com.travelDestinationPlanner.fawry.dto.DestinationRequestDto;
import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-02T16:57:09+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25-loom (Oracle Corporation)"
)
@Component
public class DestinationMapperImpl implements DestinationMapper {

    @Override
    public Destination toEntity(DestinationRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Destination.DestinationBuilder destination = Destination.builder();

        destination.countryName( requestDto.getCountryName() );
        destination.capital( requestDto.getCapital() );
        destination.region( requestDto.getRegion() );
        destination.population( requestDto.getPopulation() );
        destination.currency( requestDto.getCurrency() );
        destination.currencySymbol( requestDto.getCurrencySymbol() );
        destination.flagUrl( requestDto.getFlagUrl() );
        destination.approved( requestDto.getApproved() );

        return destination.build();
    }

    @Override
    public DestinationResponseDto toResponseDto(Destination destination) {
        if ( destination == null ) {
            return null;
        }

        DestinationResponseDto.DestinationResponseDtoBuilder destinationResponseDto = DestinationResponseDto.builder();

        destinationResponseDto.id( destination.getId() );
        destinationResponseDto.countryName( destination.getCountryName() );
        destinationResponseDto.capital( destination.getCapital() );
        destinationResponseDto.region( destination.getRegion() );
        destinationResponseDto.population( destination.getPopulation() );
        destinationResponseDto.currency( destination.getCurrency() );
        destinationResponseDto.currencySymbol( destination.getCurrencySymbol() );
        destinationResponseDto.flagUrl( destination.getFlagUrl() );

        return destinationResponseDto.build();
    }

    @Override
    public void updateEntityFromRequest(DestinationRequestDto requestDto, Destination destination) {
        if ( requestDto == null ) {
            return;
        }

        destination.setCountryName( requestDto.getCountryName() );
        destination.setCapital( requestDto.getCapital() );
        destination.setRegion( requestDto.getRegion() );
        destination.setPopulation( requestDto.getPopulation() );
        destination.setCurrency( requestDto.getCurrency() );
        destination.setCurrencySymbol( requestDto.getCurrencySymbol() );
        destination.setFlagUrl( requestDto.getFlagUrl() );
        destination.setApproved( requestDto.getApproved() );
    }
}
