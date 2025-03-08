package com.wiormiw.simple_perpustakaan_online.models.dto.rent;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentHistoryResponseDTO(
        UUID id,
        String bookName,
        String rentedBy,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime deadline,
        String rentStatus
) {}
