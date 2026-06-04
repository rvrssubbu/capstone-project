package com.paypilot.common.api;

import com.paypilot.common.web.CorrelationId;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public abstract class CommonExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

  @ExceptionHandler(ApiException.class)
  protected ResponseEntity<ApiError> handleApiException(ApiException ex) {
    log.warn("Handled API exception code={} status={} message={}", ex.code(), ex.status().value(), ex.getMessage());
    return ResponseEntity.status(ex.status())
        .body(ApiError.of(CorrelationId.current(), ex.code(), ex.getMessage(), List.of()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
    List<String> details = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .toList();
    log.warn("Request validation failed details={}", details);
    return ResponseEntity.badRequest()
        .body(ApiError.of(CorrelationId.current(), "VALIDATION_ERROR", "Request validation failed", details));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
    List<String> details = ex.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .toList();
    log.warn("Constraint validation failed details={}", details);
    return ResponseEntity.badRequest()
        .body(ApiError.of(CorrelationId.current(), "VALIDATION_ERROR", "Request validation failed", details));
  }

  @ExceptionHandler({
      HttpMessageNotReadableException.class,
      MethodArgumentTypeMismatchException.class,
      MissingServletRequestParameterException.class
  })
  protected ResponseEntity<ApiError> handleBadRequest(Exception ex) {
    log.warn("Bad request: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(ApiError.of(CorrelationId.current(), "BAD_REQUEST", "Malformed or invalid request", List.of()));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ApiError> handleUnexpected(Exception ex) {
    log.error("Unhandled exception", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(CorrelationId.current(), "INTERNAL_ERROR", "Unexpected server error", List.of()));
  }

  private String formatFieldError(FieldError error) {
    return error.getField() + ": " + error.getDefaultMessage();
  }
}
