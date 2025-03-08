package com.wiormiw.simple_perpustakaan_online.config.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
