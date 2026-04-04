package com.travelDestinationPlanner.fawry.controller;

import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.service.UserDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/destinations")
@RequiredArgsConstructor
public class UserDestinationController {

    private final UserDestinationService userDestinationService;

    @GetMapping
    public Page<DestinationResponseDto> getAllDestinations(
            @PageableDefault(size = 20) Pageable pageable) {
        return userDestinationService.getAllDestinations(pageable);
    }

    @GetMapping("/search")
    public Page<DestinationResponseDto> searchDestinations(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean approved,
            @PageableDefault(size = 20) Pageable pageable) {
        return userDestinationService.search(name, pageable, approved);
    }

    @GetMapping("/{id}")
    public DestinationResponseDto getDestinationById(@PathVariable Long id) {
        return userDestinationService.getDestinationById(id);
    }
}
