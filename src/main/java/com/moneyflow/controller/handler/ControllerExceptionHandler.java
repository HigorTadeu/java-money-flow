package com.moneyflow.controller.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.moneyflow.dto.error.CustomError;
import com.moneyflow.dto.error.ValidationError;
import com.moneyflow.service.exception.DatabaseException;
import com.moneyflow.service.exception.JWTCreationException;
import com.moneyflow.service.exception.ResourceNotFoundException;
import com.moneyflow.service.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomError> validation(ValidationException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<CustomError> jwtNotCreated(JWTCreationException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomError> illegalArgument(IllegalArgumentException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomError> tokenExpired(TokenExpiredException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> argumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError error = new ValidationError(Instant.now(), status.value(), "Daods inválidos", request.getRequestURI());

        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            error.addErrors(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> databaseException(DatabaseException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidUuid(IllegalArgumentException ex) {
//        ErrorResponse error = new ErrorResponse("INVALID_UUID", "Invalid UUID format");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
}
