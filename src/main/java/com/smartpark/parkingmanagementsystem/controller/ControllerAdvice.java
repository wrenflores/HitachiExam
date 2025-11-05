package com.smartpark.parkingmanagementsystem.controller;

import com.smartpark.parkingmanagementsystem.exception.ErrorConstants;
import com.smartpark.parkingmanagementsystem.exception.ExceptionResponse;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice(assignableTypes = {ParkingLotController.class, VehicleController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        String errorCode = "";

        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            var firstError = ex.getBindingResult().getFieldErrors().get(0);
            errorCode = firstError.getDefaultMessage();
        }

        log.error("Exception occurred. {} - {}", errorCode, ErrorConstants.getMessage(errorCode));
        return new ResponseEntity<>(ExceptionResponse.builder()
                .errorCode(errorCode)  // a standard code for validation failures
                .errorMessage(ErrorConstants.getMessage(errorCode))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<ExceptionResponse> handleException(GenericException ex) {
        log.error("Exception occurred. {} - {}", ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(ExceptionResponse.builder()
                .errorCode(ex.getCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidEnum(HttpMessageNotReadableException ex) {
        String errorCode = "VVC005";
        log.error("Exception occurred. {} - {}", errorCode, ErrorConstants.getMessage(errorCode));
        return new ResponseEntity<>(ExceptionResponse.builder()
                .errorCode(errorCode)  // a standard code for validation failures
                .errorMessage(ErrorConstants.getMessage(errorCode))
                .build(), HttpStatus.BAD_REQUEST);
    }
}
