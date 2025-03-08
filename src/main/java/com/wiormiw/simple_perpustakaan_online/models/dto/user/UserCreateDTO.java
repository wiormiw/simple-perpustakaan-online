package com.wiormiw.simple_perpustakaan_online.models.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Please enter email!")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Please enter a valid email address."
        )
        String email,
        @NotBlank(message = "Please enter password!")
        @Size(min = 8, max = 8, message = "Exceeded/Below acceptable digit!")
        @Pattern(
                regexp = "^(?=.*[A-Z])[A-Za-z0-9]+$",
                message = "Password must contain at least one uppercase letter and no special characters."
        )
        String password,
        @NotBlank(message = "Please enter full name!")
        String fullName,
        @NotBlank(message = "Please enter address!")
        String address,
        @NotBlank(message = "Please enter phone number!")
        @Size(max = 13, message = "Exceeded acceptable digit!")
        String phoneNumber
) {}
