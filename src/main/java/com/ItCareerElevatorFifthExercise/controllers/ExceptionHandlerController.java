package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.exceptions.EmailIsAlreadyTakenException;
import com.ItCareerElevatorFifthExercise.exceptions.InvalidCredentialsException;
import com.ItCareerElevatorFifthExercise.exceptions.MessagingMicroserviceException;
import com.ItCareerElevatorFifthExercise.exceptions.NoSuchRoleException;
import com.ItCareerElevatorFifthExercise.exceptions.NoSuchUserException;
import com.ItCareerElevatorFifthExercise.exceptions.UsernameIsAlreadyTakenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MessagingMicroserviceException.class)
    public ResponseEntity<ErrorResponseDTO> handleMessagingMicroserviceException(MessagingMicroserviceException ex) {
        log.warn("Handling MessagingMicroserviceException.");
        log.warn("Error status: {}, message: {}.", ex.getStatus(), ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getStatus(),
                ex.getMessage(),
                ex.getTimestamp()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }

    @ExceptionHandler(UsernameIsAlreadyTakenException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UsernameIsAlreadyTakenException ex) {
        log.warn("Handling UsernameIsAlreadyTakenException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid username or password.", // ! Don't tell the user explicitly
                System.currentTimeMillis()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(error);
    }

    @ExceptionHandler(EmailIsAlreadyTakenException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailIsAlreadyTakenException(EmailIsAlreadyTakenException ex) {
        log.warn("Handling EmailIsAlreadyTakenException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(error);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchUserException(NoSuchUserException ex) {
        log.warn("Handling NoSuchUserException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(error);
    }

    @ExceptionHandler(NoSuchRoleException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoSuchRoleException(NoSuchRoleException ex) {
        log.warn("Handling NoSuchRoleException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class) // * Thrown in authenticate -> catch block
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        log.warn("Handling InvalidCredentialsException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class) // ? Probably already handled in JwtRequestFilter (if it's thrown only there)
    public ResponseEntity<ErrorResponseDTO> handleException(SignatureException ex) {
        log.warn("Handling SignatureException.");

        ErrorResponseDTO error =
                new ErrorResponseDTO(
                        HttpStatus.UNAUTHORIZED.value(),
                        "Invalid token signature.",
                        System.currentTimeMillis()
                );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class) // * Expired JWT token
    public ResponseEntity<ErrorResponseDTO> handleExpiredJwtException(ExpiredJwtException ex) {
        log.warn("Handling ExpiredJwtException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid or missing authentication credentials.",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // * Thrown when an argument annotated with @Valid fails validation checks
    public ResponseEntity<ErrorResponseDTO> handleException(MethodArgumentNotValidException ex) {
        log.warn("Handling MethodArgumentNotValidException.");

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Request data validation failed.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                errorMessage,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(ConstraintViolationException.class) // * Thrown when validation constraints on method parameters (e.g., path variables, query parameters) or method return values fail. This requires the containing class (Controller/Service) to be annotated with @Validated.
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Handling ConstraintViolationException.");

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Request data validation failed.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                errorMessage,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class) // * Thrown when the incoming HTTP request body cannot be converted to the required object type (malformed JSON, incorrect data type for a field)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(HttpMessageNotReadableException ex) {
        log.warn("Handling HttpMessageNotReadableException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Request body contains values that do not match the expected data types.",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // * Thrown when user makes a type of request on an endpoint, that is not supported/defined.
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(HttpRequestMethodNotSupportedException ex) {
        log.warn("Handling HttpRequestMethodNotSupportedException.");

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                String.format("Method %s is not supported for this endpoint.", ex.getMethod()),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }
}
