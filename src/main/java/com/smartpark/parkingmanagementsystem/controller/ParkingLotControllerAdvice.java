package com.smartpark.parkingmanagementsystem.controller;

import com.smartpark.parkingmanagementsystem.exception.ErrorConstants;
import com.smartpark.parkingmanagementsystem.exception.ExceptionResponse;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {ParkingLotController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ParkingLotControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        String errorMessage = "";

        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            var firstError = ex.getBindingResult().getFieldErrors().get(0);
            errorMessage = firstError.getDefaultMessage();
        }

        return new ResponseEntity<>(ExceptionResponse.builder()
                .errorCode(errorMessage)  // a standard code for validation failures
                .errorMessage(ErrorConstants.getMessage(errorMessage))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<ExceptionResponse> handleException(GenericException ex) {
//        log.error("Exception occurred. {} - {}", ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(ExceptionResponse.builder()
                .errorCode(ex.getCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
