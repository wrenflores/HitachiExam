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
        ERRORS.put("PID001", "ParkingLot %s already exists");
        ERRORS.put("PID002", "ParkingLot not found");

        ERRORS.put("VVC001", "License plate can only contain letters, numbers, and dashes");
        ERRORS.put("VVC002", "Owner name not blank");
        ERRORS.put("VVC003", "Owner name can only contain letters and spaces");
        ERRORS.put("VVC004", "Invalid vehicle type");
        ERRORS.put("VVC005", "Invalid vehicle type. Allowed values: CAR, MOTORCYCLE, TRUCK.");

        ERRORS.put("VID001", "Vehicle %s already exists");
        ERRORS.put("VID002", "Vehicle not found");
        ERRORS.put("VCI001", "Vehicle has no active check-in record or assigned parking lot.");


        ERRORS.put("PFL001", "Full Parking");

        ERRORS.put("USR001","User not found");

        ERRORS.put("LGN001","Invalid username or password");


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
