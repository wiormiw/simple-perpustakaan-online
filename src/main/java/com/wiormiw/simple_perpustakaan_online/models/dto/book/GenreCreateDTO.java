package com.wiormiw.simple_perpustakaan_online.models.dto.book;

import jakarta.validation.constraints.NotBlank;

public record GenreCreateDTO(
        @NotBlank(message = "Please enter genre name!")
        String name
) {}
