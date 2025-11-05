package com.smartpark.parkingmanagementsystem.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorConstants {

    // Map of all error codes and their default messages
    public static final Map<String, String> ERRORS;

    static {
        ERRORS = new HashMap<>();

        // Validation errors
        ERRORS.put("VCP001", "Invalid Parking ID");
        ERRORS.put("VCP002", "Invalid Parking ID");
        ERRORS.put("VCP003", "Lot ID cannot exceed 50 characters");

        ERRORS.put("VCP004", "Invalid Location");
        ERRORS.put("VCP005", "Invalid Location");

        ERRORS.put("VCP006", "Invalid Capacity");

        ERRORS.put("VCP007", "Invalid Cost per minute");


        // Parking lot errors
        ERRORS.put("PNF001", "ParkingLot not found");

        // Generic errors
        ERRORS.put("INTERNAL_ERROR", "An unexpected error occurred");
        ERRORS.put("INVALID_REQUEST", "Invalid request data");

        // Add more errors here as needed
    }

    // Optional utility to get message by code
    public static String getMessage(String code) {
        return ERRORS.getOrDefault(code, "Unknown error");
    }
}
