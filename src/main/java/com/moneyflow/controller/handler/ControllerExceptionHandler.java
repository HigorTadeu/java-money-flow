package com.moneyflow.controller.handler;

import com.moneyflow.dto.error.CustomError;
import com.moneyflow.service.exception.JWTCreationException;
import com.moneyflow.service.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<CustomError> jwtNotCreated(JWTCreationException exception, HttpServletRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomError> illegalArgument(IllegalArgumentException exception, HttpServletRequest request){
        HttpStatus status= HttpStatus.CONFLICT;
        CustomError error = new CustomError(Instant.now(), status.value(), exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidUuid(IllegalArgumentException ex) {
//        ErrorResponse error = new ErrorResponse("INVALID_UUID", "Invalid UUID format");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
}
