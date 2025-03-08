package com.wiormiw.simple_perpustakaan_online.models.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record BookCreateDTO(
        @NotBlank(message = "Please enter a title!")
        String title,
        @Min(value = 1, message = "Quantity must be at least 1!")
        int quantity,
        @NotEmpty(message = "Please select at least one genre!")
        Set<UUID> genreIds
) {}
