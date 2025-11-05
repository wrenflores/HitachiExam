package com.smartpark.parkingmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseParkingLotDTO {

    private String lotId;

    private String location;

    private int capacity;

    private double costPerMinute;

    private List<ResponseVehicleDTO> vehicles;

}
