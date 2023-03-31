package com.ada.tech.movies_battle.common;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerController {
    private final MessageSource messageSource;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(AccessDeniedException ex) {
        var message = ex.getMessage();
        log.error("AccessDeniedException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> responseStatusException(ResponseStatusException ex) {
        var message = ex.getReason();
        log.error("ResponseStatusException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Object> httpStatusCodeException(HttpStatusCodeException ex) {
        var message = ex.getResponseBodyAsString();
        log.error("HttpStatusCodeException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> feignException(FeignException ex) {
        var message = ex.contentUTF8();
        var status = HttpStatus.valueOf(ex.status());
        log.error("FeignException: code: {} body {}", status, message);
        return factoryResponseEntityWithResponseEntityException(message, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var message = getStringErrors(ex.getBindingResult());
        log.error("MethodArgumentNotValidException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        var message = ex.getMessage();
        log.error("HttpRequestMethodNotSupportedException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var message = getInternalException(ex, InvalidFormatException.class)
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());
        log.error("HttpMessageNotReadableException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        var message = ofNullable(ex.getRootCause())
                .map(Throwable::getMessage)
                .orElse("dataIntegrityViolationException");
        log.error("DataIntegrityViolationException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> missingRequestHeaderException(MissingRequestHeaderException ex) {
        var message = "Header '" + ex.getHeaderName() + "' is required";
        log.error("MissingRequestHeaderException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        var message = "Parameter '" + ex.getParameterName() + "' type '" + ex.getParameterType() + "' is required";
        log.error("MissingRequestHeaderException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> bindException(BindException ex) {
        var message = "Query params errors, " + getStringErrors(ex.getBindingResult());
        log.error("BindException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {
        log.error("BindException: {}", ex.getMessage());
        return factoryResponseEntityWithResponseEntityException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        var message = String.format(
                "Failed to convert parameter %s, value %s, to %s",
                ex.getName(),
                ex.getValue(),
                ofNullable(ex.getRequiredType())
                        .map(Class::getSimpleName)
                        .orElse("Unknown")
        );
        log.error("MethodArgumentTypeMismatchException: {}", message);
        return factoryResponseEntityWithResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({JWTVerificationException.class, JWTCreationException.class})
    public ResponseEntity<Object> jwtException(Exception ex) {
        log.error("JwtException", ex);
        return factoryResponseEntityWithResponseEntityException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsException(BadCredentialsException ex) {
        log.error("BadCredentialsException", ex);
        return factoryResponseEntityWithResponseEntityException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AssertionError.class)
    public ResponseEntity<Object> assertionError(AssertionError ex) {
        log.error("AssertionError: {}", ex.getMessage(), ex);
        return factoryResponseEntityWithResponseEntityException(getAllMessageFromException(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        log.error("Exception: {}", ex.getClass().getName(), ex);
        return factoryResponseEntityWithResponseEntityException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getStringErrors(BindingResult bindingResult) {
        return bindingResult
                .getFieldErrors().stream()
                .map(it -> it.getField() + ": " + it.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }

    private ResponseEntity<Object> factoryResponseEntityWithResponseEntityException(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(this.messageSource.getMessage(message, null, message, Locale.ROOT))
                        .status(httpStatus)
                        .build(),
                httpStatus
        );
    }

    private String getAllMessageFromException(Throwable throwable) {
        var messages = new ArrayList<String>(5);
        Throwable rootCause = throwable;
        do {
            messages.add(rootCause.getClass().getSimpleName() + ": " + rootCause.getMessage());
            rootCause = rootCause.getCause();
        } while (nonNull(rootCause));
        return String.join(" && ", messages);
    }

    private <T> Optional<Throwable> getInternalException(Throwable throwable, Class<T> tClass) {
        do {
            if (tClass.isInstance(throwable)) {
                return Optional.of(throwable);
            }
            throwable = throwable.getCause();
        } while (nonNull(throwable));
        return Optional.empty();
    }
}
