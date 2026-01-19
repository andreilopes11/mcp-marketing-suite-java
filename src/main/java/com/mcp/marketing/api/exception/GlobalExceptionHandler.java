package com.mcp.marketing.api.exception;

import com.mcp.marketing.api.context.RequestContextAttributes;
import com.mcp.marketing.api.dto.ErrorResponse;
import com.mcp.marketing.api.util.RequestIdResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler translating validation/runtime errors into a consistent envelope.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final RequestIdResolver requestIdResolver;

    public GlobalExceptionHandler(RequestIdResolver requestIdResolver) {
        this.requestIdResolver = requestIdResolver;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());
        ErrorResponse errorResponse = ErrorResponse.validation(resolveRequestId(request), request.getRequestURI(), fieldErrors, executionTime(request));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());
        ErrorResponse errorResponse = ErrorResponse.validation(resolveRequestId(request), request.getRequestURI(), fieldErrors, executionTime(request));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.warn("invalid payload path={}", request.getRequestURI(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(resolveRequestId(request), HttpStatus.BAD_REQUEST.value(),
                "INVALID_PAYLOAD", "Request body is missing or malformed", request.getRequestURI(), executionTime(request));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        logger.warn("invalid argument path={}", request.getRequestURI(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(resolveRequestId(request), HttpStatus.BAD_REQUEST.value(),
                "INVALID_ARGUMENT", ex.getMessage(), request.getRequestURI(), executionTime(request));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        logger.error("runtime error path={} message={}", request.getRequestURI(), ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(resolveRequestId(request), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR", ex.getMessage(), request.getRequestURI(), executionTime(request));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        logger.error("unexpected error path={} message={}", request.getRequestURI(), ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.of(resolveRequestId(request), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "UNEXPECTED_ERROR", ex.getMessage(), request.getRequestURI(), executionTime(request));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ErrorResponse.FieldError toFieldError(FieldError fieldError) {
        return ErrorResponse.FieldError.builder()
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    private ErrorResponse.FieldError toFieldError(ConstraintViolation<?> violation) {
        return ErrorResponse.FieldError.builder()
                .field(violation.getPropertyPath().toString())
                .rejectedValue(violation.getInvalidValue() == null ? null : violation.getInvalidValue().toString())
                .message(violation.getMessage())
                .build();
    }

    private String resolveRequestId(HttpServletRequest request) {
        Object existing = request.getAttribute(RequestContextAttributes.REQUEST_ID);
        if (existing instanceof String id && !id.isBlank()) {
            return id;
        }
        String generated = requestIdResolver.resolve(request);
        request.setAttribute(RequestContextAttributes.REQUEST_ID, generated);
        return generated;
    }

    private long executionTime(HttpServletRequest request) {
        Object attr = request.getAttribute(RequestContextAttributes.START_TIME);
        long startTime = attr instanceof Long value ? value : System.currentTimeMillis();
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.max(0, elapsed);
    }
}
