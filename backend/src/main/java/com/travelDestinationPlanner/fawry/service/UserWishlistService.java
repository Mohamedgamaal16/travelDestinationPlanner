package com.travelDestinationPlanner.fawry.service;

import com.travelDestinationPlanner.fawry.dto.UserWishlistResponseDto;
import com.travelDestinationPlanner.fawry.dto.WishlistAddRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserWishlistService {

    Page<UserWishlistResponseDto> getWishlist(Pageable pageable);

    UserWishlistResponseDto addToWishlist(WishlistAddRequestDto request);

    void removeFromWishlist(Long destinationId);
}
