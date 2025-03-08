package com.wiormiw.simple_perpustakaan_online.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleException(ResourceNotFoundException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleException(ConflictException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ApiError> handleException(UnprocessableEntityException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest r) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorDetails.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
        }

        String summaryMessage = errorDetails.size() == 1
                ? "There’s an issue with your input. Please fix it below."
                : "There were " + errorDetails.size() + " issues with your input. Please check the details below.";

        ApiError apiError = new ApiError(
                r.getRequestURI(),
                summaryMessage,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                errorDetails
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleException(BadRequestException e, HttpServletRequest r) {
        ApiError apiError = new ApiError(
                r.getRequestURI(),
                e.getMessage(), // Use exception message directly
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                null // No additional details
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleException(IllegalArgumentException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(), // Use exception message directly
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                null // No additional details
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleException(IllegalStateException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(), // Use exception message directly
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                null // No additional details
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ApiError> handleException(ImageProcessingException e, HttpServletRequest r) {
        ApiError apiError = new ApiError(
                r.getRequestURI(),
                e.getMessage(), // Use exception message directly
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                LocalDateTime.now(),
                null // No additional details
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ApiError> handleException(Exception e, HttpServletRequest r) {
        ApiError apiError = new ApiError(
                r.getRequestURI(),
                "An unexpected error occurred. Please try again later or contact support.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                null // No additional details
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
