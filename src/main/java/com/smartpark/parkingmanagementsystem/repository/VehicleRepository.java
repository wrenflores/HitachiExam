package com.smartpark.parkingmanagementsystem.repository;


import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    // Find all vehicles of a specific type (e.g., CAR, MOTORCYCLE)
    List<Vehicle> findByType(VehicleType type);

    // Find all vehicles owned by a specific person
    List<Vehicle> findByOwnerNameContainingIgnoreCase(String ownerName);

    // Find vehicles by parking lot ID
    List<Vehicle> findByParkingLot_LotId(String lotId);
}

