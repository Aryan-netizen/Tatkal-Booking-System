package com.example.Tatkal.Exception;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.Tatkal.Dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        Map<String, String> validationErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.putIfAbsent(error.getField(),
                        error.getDefaultMessage()));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Please correct the highlighted fields and try again.",
                request,
                validationErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {

        Map<String, String> validationErrors = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                validationErrors.putIfAbsent(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "Please correct the request values and try again.",
                request,
                validationErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Malformed request",
                "Request body is missing or contains invalid data.",
                request,
                Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        String type = exception.getRequiredType() == null
                ? "value"
                : exception.getRequiredType().getSimpleName();
        String message = "Parameter '" + exception.getName()
                + "' must be a valid " + type + ".";
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid parameter", message,
                request, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "Conflict",
                "The request conflicts with existing data. Check for duplicate or related records.",
                request,
                Map.of());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            RuntimeException exception,
            HttpServletRequest request) {

        String message = exception.getMessage() == null
                ? "The request could not be completed."
                : exception.getMessage();
        HttpStatus status = statusFor(message);
        return buildResponse(status, status.getReasonPhrase(), message, request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Request failed",
                "The request could not be completed. Please try again.",
                request,
                Map.of());
    }

    private HttpStatus statusFor(String message) {
        String normalized = message.toLowerCase();
        if (normalized.contains("not found")) {
            return HttpStatus.NOT_FOUND;
        }
        if (normalized.contains("already exists")
                || normalized.contains("duplicate")
                || normalized.contains("no seats")
                || normalized.contains("already cancelled")
                || normalized.contains("already assigned")) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request,
            Map<String, String> validationErrors) {
        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                error,
                message,
                request.getRequestURI(),
                validationErrors);
        return ResponseEntity.status(status).body(response);
    }
}