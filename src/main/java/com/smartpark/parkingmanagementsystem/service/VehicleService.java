package com.smartpark.parkingmanagementsystem.service;

import com.smartpark.parkingmanagementsystem.dto.CreateVehicleDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleCheckoutDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleDTO;

public interface VehicleService {

    CreateVehicleDTO createVehicle(CreateVehicleDTO createVehicleDTO);

    ResponseVehicleDTO checkInVehicle(String licensePlate, String lotId);

    ResponseVehicleCheckoutDTO checkOutVehicle(String licensePlate);

}
