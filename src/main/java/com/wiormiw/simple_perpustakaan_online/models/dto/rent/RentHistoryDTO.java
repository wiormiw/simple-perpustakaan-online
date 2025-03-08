package com.wiormiw.simple_perpustakaan_online.models.dto.rent;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentHistoryDTO(
        UUID id,
        UUID rentId,
        UUID bookId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime deadline,
        String status
) {}
