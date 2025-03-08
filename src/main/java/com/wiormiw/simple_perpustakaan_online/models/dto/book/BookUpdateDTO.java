package com.wiormiw.simple_perpustakaan_online.models.dto.book;

import jakarta.validation.constraints.Min;

import java.util.Set;
import java.util.UUID;

public record BookUpdateDTO(
        String title,
        @Min(value = 1, message = "Quantity must be at least 1!")
        Integer quantity,
        Set<UUID> genreIds
) {}
