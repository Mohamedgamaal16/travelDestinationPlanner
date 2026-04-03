package com.travelDestinationPlanner.fawry.controller;

import com.travelDestinationPlanner.fawry.dto.DestinationRequestDto;
import com.travelDestinationPlanner.fawry.dto.DestinationResponseDto;
import com.travelDestinationPlanner.fawry.service.AdminDestinationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/destinations")
@RequiredArgsConstructor
public class AdminDestinationController {

    private final AdminDestinationService adminDestinationService;

    @GetMapping("/suggestions")
    public List<DestinationResponseDto> getSuggestions() {
        return adminDestinationService.getSuggestions();
    }

    @PostMapping
    public DestinationResponseDto addDestination(
            @Valid @RequestBody DestinationRequestDto dto) {
        return adminDestinationService.addDestination(dto);
    }

    @DeleteMapping("/{id}")
public ResponseEntity<String> deleteDestination(@PathVariable Long id) {
    adminDestinationService.deleteDestination(id);
    return ResponseEntity.ok("Destination deleted successfully");
}

    @PostMapping("/bulk")
    public List<DestinationResponseDto> bulkAdd(
            @Valid @RequestBody List<DestinationRequestDto> dtos) {
        return adminDestinationService.bulkAdd(dtos);
    }
}
