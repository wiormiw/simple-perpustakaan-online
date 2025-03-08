package com.wiormiw.simple_perpustakaan_online.models.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RoleCreateDTO(
        @NotBlank(message = "Please enter role name!")
        String name
) {}
