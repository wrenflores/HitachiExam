package com.smartpark.parkingmanagementsystem.service.impl;

import com.smartpark.parkingmanagementsystem.dto.CreateParkingLotDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingAvailabilityDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingLotDTO;
import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.exception.ErrorConstants;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import com.smartpark.parkingmanagementsystem.repository.ParkingLotRepository;
import com.smartpark.parkingmanagementsystem.service.ParkingLotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    @Autowired
    private ParkingLotRepository lotRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Page<ResponseParkingLotDTO> getAllParkingLots(Pageable pageable) {
        Page<ParkingLot> parkingLotsPage = lotRepository.findAll(pageable);

        return parkingLotsPage.map(parkingLot ->
                modelMapper.map(parkingLot, ResponseParkingLotDTO.class)
        );
    }

    @Override
    public ResponseParkingLotDTO createParkingLot(CreateParkingLotDTO createParkingLotDTO) {

        if (lotRepository.existsById(createParkingLotDTO.getLotId())) {
            throw new GenericException("PID001",
                    "ParkingLot with ID " + createParkingLotDTO.getLotId() + " already exists"
            );
        }


        ParkingLot parkingLot = modelMapper.map(createParkingLotDTO, ParkingLot.class);
        return modelMapper.map(lotRepository.save(parkingLot), ResponseParkingLotDTO.class);
    }

    @Override
    public ResponseParkingAvailabilityDTO getParkingLotAvailability(String parkingLotId) {
        ParkingLot parkingLot = lotRepository.findById(parkingLotId).orElseThrow(
                () -> new GenericException("PNF001",ErrorConstants.getMessage("PNF001"))
        );
        return ResponseParkingAvailabilityDTO.builder()
                .capacity(parkingLot.getCapacity())
                .occupiedSpaces(parkingLot.getOccupiedSpaces())
                .availableSpaces(parkingLot.getAvailableSpaces())
                .build();
    }

    @Override
    public ResponseParkingLotDTO getParkingLot(String parkinglotId) {
        return modelMapper.map(
                lotRepository.findById(parkinglotId).orElseThrow(
                () -> new GenericException("PNF001",ErrorConstants.getMessage("PNF001"))
        ), ResponseParkingLotDTO.class);
    }
}
