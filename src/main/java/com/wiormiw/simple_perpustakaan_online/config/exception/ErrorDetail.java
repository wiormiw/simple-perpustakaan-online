package com.wiormiw.simple_perpustakaan_online.config.exception;

public record ErrorDetail(
        String field,
        String message
) {}
