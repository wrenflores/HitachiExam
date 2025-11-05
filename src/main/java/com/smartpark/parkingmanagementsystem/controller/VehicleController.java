package com.smartpark.parkingmanagementsystem.controller;

import com.smartpark.parkingmanagementsystem.dto.CreateVehicleDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleCheckoutDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleDTO;
import com.smartpark.parkingmanagementsystem.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<CreateVehicleDTO> registerVehicle(@Valid @RequestBody CreateVehicleDTO createVehicleDTO){
        return ResponseEntity.ok(vehicleService.createVehicle(createVehicleDTO));
    }

    @PatchMapping("/checkin/{licensePlate}")
    public ResponseEntity<ResponseVehicleDTO> checkInVehicle(@PathVariable String licensePlate,
                                                             @RequestParam String lotId){
        return ResponseEntity.ok(vehicleService.checkInVehicle(licensePlate, lotId));
    }

    @PatchMapping("/checkout/{licensePlate}")
    public ResponseEntity<ResponseVehicleCheckoutDTO> checkOutVehicle(@PathVariable String licensePlate){
        return ResponseEntity.ok(vehicleService.checkOutVehicle(licensePlate));
    }

}
