package com.travelDestinationPlanner.fawry.controller;

import com.travelDestinationPlanner.fawry.dto.UserWishlistResponseDto;
import com.travelDestinationPlanner.fawry.dto.WishlistAddRequestDto;
import com.travelDestinationPlanner.fawry.service.UserWishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/wishlist")
@RequiredArgsConstructor
public class UserWishlistController {

    private final UserWishlistService userWishlistService;

    @GetMapping
    public Page<UserWishlistResponseDto> getWishlist(
            @PageableDefault(size = 20) Pageable pageable) {
        return userWishlistService.getWishlist(pageable);
    }

    @PostMapping
    public UserWishlistResponseDto addToWishlist(@Valid @RequestBody WishlistAddRequestDto request) {
        return userWishlistService.addToWishlist(request);
    }

    @DeleteMapping("/{destinationId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long destinationId) {
        userWishlistService.removeFromWishlist(destinationId);
        return ResponseEntity.noContent().build();
    }
}
