package com.travelDestinationPlanner.fawry.repository;

import com.travelDestinationPlanner.fawry.entity.Destination;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Page<Destination> findByApprovedTrue(Pageable pageable);

    Page<Destination> findByApprovedTrueAndCountryNameContainingIgnoreCase(String countryName, Pageable pageable);

    Page<Destination> findByApprovedFalse(Pageable pageable);

    Page<Destination> findByApprovedFalseAndCountryNameContainingIgnoreCase(String countryName, Pageable pageable);

    Optional<Destination> findByIdAndApprovedTrue(Long id);
}
