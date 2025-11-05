package com.smartpark.parkingmanagementsystem.dto;

import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseVehicleCheckoutDTO {

    private String licensePlate;

    private String ownerName;

    private VehicleType type;

    private String lotId;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private double parkingCost;

}
