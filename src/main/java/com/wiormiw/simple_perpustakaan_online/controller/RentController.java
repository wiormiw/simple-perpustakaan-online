package com.wiormiw.simple_perpustakaan_online.controller;

import com.wiormiw.simple_perpustakaan_online.models.CustomUserDetails;
import com.wiormiw.simple_perpustakaan_online.models.dto.rent.RentDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.rent.RentHistoryResponseDTO;
import com.wiormiw.simple_perpustakaan_online.service.implementation.RentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rent")
public class RentController {
    private final RentServiceImpl rentServiceImpl;

    public RentController(
            RentServiceImpl rentServiceImpl
    ) {
        this.rentServiceImpl = rentServiceImpl;
    }

    @PutMapping("/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RentHistoryResponseDTO> rentBook(
            @PathVariable UUID bookId
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        UUID userId = userDetails.getId();
        return ResponseEntity.ok(rentServiceImpl.rentBook(userId, bookId));
    }

    @PutMapping("/{rentId}/return")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RentHistoryResponseDTO> returnBook(
            @PathVariable UUID rentId
    ) {
        return ResponseEntity.ok(rentServiceImpl.returnBook(rentId));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RentHistoryResponseDTO> getUserRent() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        UUID userId = userDetails.getId();
        RentHistoryResponseDTO rent = rentServiceImpl.getUserRent(userId);
        return ResponseEntity.ok(rent);
    }

    @GetMapping("/user/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RentHistoryResponseDTO>> getUserRentHistories() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        UUID userId = userDetails.getId();
        List<RentHistoryResponseDTO> rentsHistory = rentServiceImpl.getUserRentHistories(userId);
        return ResponseEntity.ok(rentsHistory);
    }

    @GetMapping("/all-rents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentDTO>> getAllRents() {
        List<RentDTO> allRents = rentServiceImpl.getAllRents();
        return ResponseEntity.ok(allRents);
    }

    @GetMapping("/all-rents/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentHistoryResponseDTO>> getAllRentsHistories() {
        List<RentHistoryResponseDTO> histories = rentServiceImpl.getAllRentsHistories();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/all-rents/active-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getActiveRentCount() {
        return ResponseEntity.ok(rentServiceImpl.getTotalActiveRents());
    }

    @GetMapping("/all-rents/recent-history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentHistoryResponseDTO>> getAllRecentRentsHistories(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<RentHistoryResponseDTO> histories = rentServiceImpl.getRecentRentHistory(limit);
        return ResponseEntity.ok(histories);
    }
}
