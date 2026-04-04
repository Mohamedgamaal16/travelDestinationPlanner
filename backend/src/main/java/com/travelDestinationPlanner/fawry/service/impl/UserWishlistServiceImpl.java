package com.travelDestinationPlanner.fawry.service.impl;

import com.travelDestinationPlanner.fawry.dto.UserWishlistResponseDto;
import com.travelDestinationPlanner.fawry.dto.WishlistAddRequestDto;
import com.travelDestinationPlanner.fawry.entity.Destination;
import com.travelDestinationPlanner.fawry.entity.User;
import com.travelDestinationPlanner.fawry.entity.UserWishlist;
import com.travelDestinationPlanner.fawry.exception.TravelDestinationPlannerApiException;
import com.travelDestinationPlanner.fawry.mapper.UserWishlistMapper;
import com.travelDestinationPlanner.fawry.repository.DestinationRepository;
import com.travelDestinationPlanner.fawry.repository.UserRepository;
import com.travelDestinationPlanner.fawry.repository.UserWishlistRepository;
import com.travelDestinationPlanner.fawry.service.UserWishlistService;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserWishlistServiceImpl implements UserWishlistService {

    private final UserWishlistRepository userWishlistRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final UserWishlistMapper userWishlistMapper;



    @Override
    @Transactional(readOnly = true)
    public Page<UserWishlistResponseDto> getWishlist(Pageable pageable) {
        User currentUser = resolveCurrentUser();
        return userWishlistRepository
                .findByUser_IdOrderByAddedAtDesc(currentUser.getId(), pageable)
                .map(userWishlistMapper::toResponseDto);
    }

    @Override
    @Transactional
    public UserWishlistResponseDto addToWishlist(WishlistAddRequestDto request) {
        User currentUser = resolveCurrentUser();
        if (userWishlistRepository.existsByUser_IdAndDestination_Id(
                currentUser.getId(), request.getDestinationId())) {
            throw new TravelDestinationPlannerApiException(
                    "Destination is already in your wishlist", HttpStatus.CONFLICT);
        }
        Destination destination = destinationRepository
                .findByIdAndApprovedTrue(request.getDestinationId())
                .orElseThrow(() -> new TravelDestinationPlannerApiException(
                        "Destination not found: " + request.getDestinationId(), HttpStatus.NOT_FOUND));

        UserWishlist saved = userWishlistRepository.save(UserWishlist.builder()
                .user(currentUser)
                .destination(destination)
                .addedAt(LocalDateTime.now())
                .build());
        return userWishlistMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long destinationId) {
        User currentUser = resolveCurrentUser();
        UserWishlist entry = userWishlistRepository
                .findByUser_IdAndDestination_Id(currentUser.getId(), destinationId)
                .orElseThrow(() -> new TravelDestinationPlannerApiException(
                        "Wishlist entry not found for destination: " + destinationId, HttpStatus.NOT_FOUND));
        userWishlistRepository.delete(entry);
    }

    private User resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new TravelDestinationPlannerApiException("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }
        String email = authentication.getName();
        return userRepository
                .findFirstByEmail(email)
                .orElseThrow(() -> new TravelDestinationPlannerApiException(
                        "User not found for principal: " + email, HttpStatus.NOT_FOUND));
    }
}
