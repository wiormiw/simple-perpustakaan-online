package com.wiormiw.simple_perpustakaan_online.config.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime,
        List<ErrorDetail> errorDetails
) {}

