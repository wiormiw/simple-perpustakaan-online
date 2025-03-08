package com.wiormiw.simple_perpustakaan_online.models.dto.user;

import java.util.UUID;

public record UserProfileDTO(
        UUID id,
        String email,
        boolean rentingStatus,
        ProfileDTO profile
) {}
