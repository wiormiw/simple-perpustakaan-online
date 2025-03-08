package com.wiormiw.simple_perpustakaan_online.models.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Pattern(
                regexp = "^(?=.*[A-Z])[A-Za-z0-9]+$",
                message = "Password must contain at least one uppercase letter and no special characters."
        )
        @Size(min = 8, max = 8, message = "Exceeded/Below acceptable digit!")
        String password,
        String fullName,
        String address,
        @Size(max = 13, message = "Exceeded acceptable digit!")
        String phoneNumber
) {}
