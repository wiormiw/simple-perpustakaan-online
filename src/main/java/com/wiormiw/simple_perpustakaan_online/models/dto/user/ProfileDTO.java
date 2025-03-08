package com.wiormiw.simple_perpustakaan_online.models.dto.user;

public record ProfileDTO(
        String fullName,
        String address,
        String profileUrl,
        String phoneNumber
) {}
