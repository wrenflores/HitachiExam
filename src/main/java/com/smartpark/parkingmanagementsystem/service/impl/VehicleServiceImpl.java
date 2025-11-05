package com.smartpark.parkingmanagementsystem.service.impl;

import com.smartpark.parkingmanagementsystem.dto.CreateVehicleDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleCheckoutDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleDTO;
import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.exception.ErrorConstants;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import com.smartpark.parkingmanagementsystem.repository.ParkingLotRepository;
import com.smartpark.parkingmanagementsystem.repository.VehicleRepository;
import com.smartpark.parkingmanagementsystem.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingLotRepository lotRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CreateVehicleDTO createVehicle(CreateVehicleDTO createVehicleDTO) {

        if (vehicleRepository.existsById(createVehicleDTO.getLicensePlate())) {
            throw new GenericException("VID001",
                    ErrorConstants.getMessage("VID001").formatted(createVehicleDTO.getLicensePlate())
            );
        }

        Vehicle vehicle = modelMapper.map(createVehicleDTO, Vehicle.class);
        return modelMapper.map(vehicleRepository.save(vehicle), CreateVehicleDTO.class);

    }

    @Override
    public ResponseVehicleDTO checkInVehicle(String licensePlate, String lotId) {
        Vehicle vehicle = vehicleRepository.findById(licensePlate).orElseThrow(
                () -> new GenericException("VID002", ErrorConstants.getMessage("VID002"))
        );

        ParkingLot parkingLot = lotRepository.findById(lotId).orElseThrow(
                () -> new GenericException("PID002",ErrorConstants.getMessage("PID002"))
        );

        if(parkingLot.isFull() ){
            throw new GenericException("PFL001", ErrorConstants.getMessage("PFL001"));
        }

        vehicle.setParkingLot(parkingLot);
        vehicle.setCheckInTime(LocalDateTime.now());

        vehicleRepository.save(vehicle);

        ResponseVehicleDTO vehicleDTO = modelMapper.map(vehicle, ResponseVehicleDTO.class);
        vehicleDTO.setLotId(parkingLot.getLotId());

        return vehicleDTO;
    }

    @Override
    public ResponseVehicleCheckoutDTO checkOutVehicle(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findById(licensePlate).orElseThrow(
                () -> new GenericException("VID002", ErrorConstants.getMessage("VID002"))
        );

        if (vehicle.getCheckInTime() == null || vehicle.getParkingLot() == null) {
            throw new GenericException("VCI001", ErrorConstants.getMessage("VCI001"));
        }

        LocalDateTime checkout = LocalDateTime.now();

        long minutes = Duration.between(vehicle.getCheckInTime(), checkout).toMinutes();

        // Avoid 0-minute cases (e.g., same-minute check-out)
        if (minutes <= 0) {
            minutes = 1;
        }

        double costPerMinute = vehicle.getParkingLot().getCostPerMinute();

        double totalCost = minutes * costPerMinute;

        ResponseVehicleCheckoutDTO vehicleCheckoutDTO = ResponseVehicleCheckoutDTO.builder()
                .licensePlate(vehicle.getLicensePlate())
                .ownerName(vehicle.getOwnerName())
                .type(vehicle.getType())
                .lotId(vehicle.getParkingLot().getLotId())
                .checkInTime(vehicle.getCheckInTime())
                .checkOutTime(checkout)
                .parkingCost(totalCost)
                .build();

        vehicle.setCheckInTime(null);
        vehicle.setParkingLot(null);

        vehicleRepository.save(vehicle);


        return vehicleCheckoutDTO;
    }
}


