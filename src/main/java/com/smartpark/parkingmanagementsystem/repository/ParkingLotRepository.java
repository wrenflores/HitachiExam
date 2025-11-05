package com.smartpark.parkingmanagementsystem.repository;


import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {
    // Example: find a parking lot by location
    ParkingLot findByLocation(String location);
}

