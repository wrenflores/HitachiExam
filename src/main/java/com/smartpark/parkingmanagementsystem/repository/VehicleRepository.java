package com.smartpark.parkingmanagementsystem.repository;


import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByCheckInTimeIsNotNull();
}

