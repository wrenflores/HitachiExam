package com.smartpark.parkingmanagementsystem.controller;

import com.smartpark.parkingmanagementsystem.dto.CreateParkingLotDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingAvailabilityDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingLotDTO;
import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.service.ParkingLotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/parkinglot")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;

    @GetMapping("/allparkinglots")
    public ResponseEntity<Page<ResponseParkingLotDTO>> getAllParkingLot(Pageable pageable){
        return ResponseEntity.ok(parkingLotService.getAllParkingLots(pageable));
    }

    @GetMapping("/availableparkinglots/{parkingLotId}")
    public ResponseEntity<ResponseParkingAvailabilityDTO> getAvailableParkingLot(@PathVariable String parkingLotId){
        return ResponseEntity.ok(parkingLotService.getParkingLotAvailability(parkingLotId));
    }

    @GetMapping("/{parkingLotId}")
    public ResponseEntity<ResponseParkingLotDTO> getAllParkingLot(@PathVariable String parkingLotId){
        return ResponseEntity.ok(parkingLotService.getParkingLot(parkingLotId));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseParkingLotDTO> registerParkingLot(@Valid @RequestBody CreateParkingLotDTO  createParkingLotDTO){
        return ResponseEntity.ok(parkingLotService.createParkingLot(createParkingLotDTO));
    }
    
}
