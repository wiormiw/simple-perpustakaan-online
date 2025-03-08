package com.wiormiw.simple_perpustakaan_online.models.dto.book;

import java.util.Set;
import java.util.UUID;

public record BookDTO(
        UUID id,
        String title,
        int quantity,
        int availableQuantity,
        String coverUrl,
        Set<UUID> genreIds
) {}
