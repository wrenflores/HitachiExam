package com.smartpark.parkingmanagementsystem.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {
    private final String code;
    private final String message;

    public GenericException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

