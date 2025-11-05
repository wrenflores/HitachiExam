package com.smartpark.parkingmanagementsystem.service;

import com.smartpark.parkingmanagementsystem.dto.CreateParkingLotDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingAvailabilityDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingLotDTO;
import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.service.impl.ParkingLotServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingLotService {

    Page<ResponseParkingLotDTO> getAllParkingLots(Pageable pageable);

    ResponseParkingLotDTO createParkingLot(CreateParkingLotDTO createParkingLotDTO);

    ResponseParkingAvailabilityDTO getParkingLotAvailability(String parkingLotId);

    ResponseParkingLotDTO getParkingLot(String parkinglotId);

}
