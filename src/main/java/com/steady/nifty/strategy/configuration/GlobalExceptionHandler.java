package com.steady.nifty.strategy.configuration;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.hibernate.StaleObjectStateException;
import org.hibernate.TransactionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.steady.nifty.strategy.exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex
     *                    the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers
     *                    HttpHeaders
     * @param status
     *                    HttpStatus
     * @param request
     *                    WebRequest
     * @return the ErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(status, request);
        error.addDetail("detail.validationError");
        error.addValidationErrors(ex.getBindingResult().getFieldErrors());
        return buildResponseEntity(error);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex
     *               the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.debug("ConstraintViolation", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, request);
        error.addDetail("detail.validationError");
        error.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(error);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex
     *                    the DataIntegrityViolationException
     * @param request
     *                    WebRequest
     * @return the ErrorResponse object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
            WebRequest request) {
        log.debug("DataIntegrityViolation", ex);
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, request);
            error.addDetail(ex.getCause().getLocalizedMessage());
            return buildResponseEntity(error);
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request);
        error.addDetail(ex.getLocalizedMessage());
        return buildResponseEntity(error);
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than
     * javax.persistence.EntityNotFoundException.
     *
     * @param ex
     *                    the EntityNotFoundException
     * @param request
     *                    WebRequest
     * @return the ErrorResponse object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, request);
        error.addDetail(ex.getMessage());
        return buildResponseEntity(error);
    }

    /**
     * Handles EntityExistsException. Created to encapsulate errors with more detail than
     * javax.persistence.EntityExistsException.
     *
     * @param ex
     *                    the EntityExistsException
     * @param request
     *                    WebRequest
     * @return the ErrorResponse object
     */
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleEntityExists(EntityExistsException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, request);
        error.addDetail(ex.getMessage());
        return buildResponseEntity(error);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientError(HttpClientErrorException ex, WebRequest request) {
        log.debug("HttpClientError", ex);
        if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, request);
            error.addDetail("detail.noResults");
            return buildResponseEntity(error);
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request);
        error.addDetail("detail.externalServiceError");
        return buildResponseEntity(error);
    }

    /**
     * Handler for bad credential errors. Ie. when an unauthenticated user tries to login with wrong username/password.
     * 
     * @param ex
     *                    the AuthenticationException
     * @param request
     *                    WebRequest
     * @return the ErrorResponse object
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleBadCredentials(AuthenticationException ex, WebRequest request) {
        // Flag for fail2ban
        log.error("Failed authentication attempt: {}", ((ServletWebRequest) request).getRequest().getRemoteAddr());

        // Log the original exception
        log.debug("AuthenticationError", ex);
        // Invalid authorization is handled by RestAuthenticationEntryPoint. Here we catch wrong password etc.
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED);
        error.addDetail("detail.authenticationError");
        return buildResponseEntity(error);
    }

    /**
     * Handler for access denied errors. Ie. when an authenticated user tries to access resources without a correct
     * role.
     * 
     * @param ex
     *                    the AccessDeniedException
     * @param request
     *                    WebRequest
     * @return the ErrorResponse object
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        // Log this on warning level for auditing purposes
        log.warn("AccessDenied", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, request);
        error.addDetail("detail.unauthorized");
        return buildResponseEntity(error);
    }

    // TODO with ETab / If-Match this should be 412 instead of 409
    @ExceptionHandler(StaleObjectStateException.class)
    protected ResponseEntity<Object> handleOptimisticLockingError(StaleObjectStateException ex, WebRequest request) {
        log.debug("HttpClientError", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, request);
        error.addDetail("detail.optimisticLockingError");
        return buildResponseEntity(error);
    }

    @ExceptionHandler(TransactionTimedOutException.class)
    protected ResponseEntity<Object> handleTimeout(TransactionTimedOutException ex, WebRequest request) {
        log.debug("TransactionTimedOut", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request);
        error.addDetail("detail.transactionTimeOut");
        return buildResponseEntity(error);
    }

    @ExceptionHandler(TransactionException.class)
    protected ResponseEntity<Object> handleTransactionError(TransactionException ex, WebRequest request) {
        log.debug("TransactionError", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, request);
        error.addDetail("detail.transactionError");
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse error) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<>(error, headers, error.getHttpStatus());
    }
}
