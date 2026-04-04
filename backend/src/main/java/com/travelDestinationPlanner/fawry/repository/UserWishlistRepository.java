package com.travelDestinationPlanner.fawry.repository;

import com.travelDestinationPlanner.fawry.entity.UserWishlist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long> {

    Page<UserWishlist> findByUser_IdOrderByAddedAtDesc(Long userId, Pageable pageable);

    Optional<UserWishlist> findByUser_IdAndDestination_Id(Long userId, Long destinationId);

    boolean existsByUser_IdAndDestination_Id(Long userId, Long destinationId);

}
