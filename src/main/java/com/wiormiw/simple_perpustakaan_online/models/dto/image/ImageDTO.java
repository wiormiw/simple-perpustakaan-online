package com.wiormiw.simple_perpustakaan_online.models.dto.image;


import jakarta.validation.constraints.NotBlank;

public record ImageDTO(
        @NotBlank(message = "Please provide an image URL!")
        String imageUrl
) {}
