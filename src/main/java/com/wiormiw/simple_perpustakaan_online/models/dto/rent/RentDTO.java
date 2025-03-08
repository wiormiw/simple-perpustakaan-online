package com.wiormiw.simple_perpustakaan_online.models.dto.rent;

import java.util.UUID;

public record RentDTO(
        UUID id,
        UUID userId,
        UUID bookId,
        String status
) {}
