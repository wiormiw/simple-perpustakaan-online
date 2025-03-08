package com.wiormiw.simple_perpustakaan_online.models.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank(message = "Please enter the email!")
        String email,
        @NotBlank(message = "Please enter the password!")
        String password
) {}
