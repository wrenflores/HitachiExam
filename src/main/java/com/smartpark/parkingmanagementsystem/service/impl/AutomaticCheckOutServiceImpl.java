package com.smartpark.parkingmanagementsystem.service.impl;

import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutomaticCheckOutServiceImpl {

    @Autowired
    private VehicleRepository vehicleRepository;

    // Runs every minute
    @Scheduled(fixedRate = 60000)
    public void removeExpiredVehicles() {
        List<Vehicle> parkedVehicles = vehicleRepository.findByCheckInTimeIsNotNull();
        for (Vehicle vehicle : parkedVehicles) {
            long minutesParked = Duration.between(vehicle.getCheckInTime(), LocalDateTime.now()).toMinutes();
            if (minutesParked >= 15) {
                log.info("Vehicle {} exceeded 15 minutes. Removing...", vehicle.getLicensePlate());

                vehicle.setCheckInTime(null);
                vehicle.setParkingLot(null);
                vehicleRepository.save(vehicle);

            }
        }
    }
}
