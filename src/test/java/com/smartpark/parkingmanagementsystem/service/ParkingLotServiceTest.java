package com.smartpark.parkingmanagementsystem.service;

import com.smartpark.parkingmanagementsystem.dto.CreateParkingLotDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingAvailabilityDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseParkingLotDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleDTO;
import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import com.smartpark.parkingmanagementsystem.repository.ParkingLotRepository;
import com.smartpark.parkingmanagementsystem.service.impl.ParkingLotServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingLotServiceTest {

    @Mock
    private ParkingLotRepository lotRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ParkingLotServiceImpl parkingLotService;


    @Test
    void testGetAllParkingLots() {
        Pageable pageable = PageRequest.of(0, 1);

        Vehicle vehicle1 = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("Jose")
                .type(VehicleType.CAR)
                .build();

        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("L1")
                .location("North Wing")
                .capacity(10)
                .costPerMinute(5.0)
                .vehicles(List.of(vehicle1))
                .build();

        Page<ParkingLot> mockPage = new PageImpl<>(List.of(parkingLot), pageable, 1);
        when(lotRepository.findAll(pageable)).thenReturn(mockPage);

        ResponseParkingLotDTO responseParkingLotDTO = ResponseParkingLotDTO.builder()
                .lotId("L1")
                .location("North Wing")
                .capacity(10)
                .vehicles(List.of(ResponseVehicleDTO.builder()
                        .licensePlate("ABC123")
                        .ownerName("Jose")
                        .type(VehicleType.CAR)
                        .build()))
                .build();

        when(modelMapper.map(parkingLot, ResponseParkingLotDTO.class)).thenReturn(responseParkingLotDTO);

        // when
        Page<ResponseParkingLotDTO> result = parkingLotService.getAllParkingLots(pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("L1", result.getContent().get(0).getLotId());
        assertEquals("ABC123", result.getContent().get(0).getVehicles().get(0).getLicensePlate());

        verify(lotRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllParkingLots_Empty() {
        Pageable pageable = PageRequest.of(0, 10);
        when(lotRepository.findAll(pageable)).thenReturn(Page.empty(pageable));

        Page<ResponseParkingLotDTO> result = parkingLotService.getAllParkingLots(pageable);

        assertTrue(result.isEmpty());
        verify(lotRepository).findAll(pageable);
    }


    @Test
    void testCreateParkingLot_Success() {
        // Given
        CreateParkingLotDTO createParkingLotDTO = CreateParkingLotDTO.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        ParkingLot mappedParkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        ParkingLot savedParkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        CreateParkingLotDTO expectedCreateParkingLotDTO = CreateParkingLotDTO.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        // When
        when(lotRepository.existsById("LOT-001")).thenReturn(false);
        when(modelMapper.map(createParkingLotDTO, ParkingLot.class)).thenReturn(mappedParkingLot);
        when(lotRepository.save(any(ParkingLot.class))).thenReturn(savedParkingLot);
        when(modelMapper.map(savedParkingLot, CreateParkingLotDTO.class)).thenReturn(expectedCreateParkingLotDTO);

        // Act
        CreateParkingLotDTO result = parkingLotService.createParkingLot(createParkingLotDTO);

        // Then
        assertNotNull(result);
        assertEquals("LOT-001", result.getLotId());
        assertEquals("Downtown Zone A", result.getLocation());

        // Verify the flow
        verify(lotRepository).existsById(createParkingLotDTO.getLotId());;
        verify(lotRepository).save(any(ParkingLot.class));
    }

    @Test
    void testCreateParkingLot_AlreadyExists_ShouldThrowException() {
        // given
        CreateParkingLotDTO createParkingLotDTO = CreateParkingLotDTO.builder()
                .lotId("L1")
                .location("North Wing")
                .capacity(50)
                .costPerMinute(10.0)
                .build();

        when(lotRepository.existsById("L1")).thenReturn(true);

        // when / then
        GenericException exception = assertThrows(GenericException.class, () ->
                parkingLotService.createParkingLot(createParkingLotDTO)
        );

        assertEquals("PID001", exception.getCode());
        verify(lotRepository, never()).save(any());
    }

    @Test
    void testGetParkingLotAvailability_Success() {
        // given
        String lotId = "L001";
        ParkingLot parkingLot = ParkingLot.builder()
                .lotId(lotId)
                .capacity(50)
                .vehicles(new ArrayList<>())
                .build();

        parkingLot.getVehicles().addAll(List.of(new Vehicle(), new Vehicle(), new Vehicle()));


        when(lotRepository.findById(lotId)).thenReturn(Optional.of(parkingLot));

        // when
        ResponseParkingAvailabilityDTO result = parkingLotService.getParkingLotAvailability(lotId);

        // then
        assertNotNull(result);
        assertEquals(50, result.getCapacity());
        assertEquals(3, result.getOccupiedSpaces());
        assertEquals(47, result.getAvailableSpaces());

        verify(lotRepository, times(1)).findById(lotId);
    }

    @Test
    void testGetParkingLotAvailability_NotFound_ShouldThrowException() {
        // given
        String lotId = "INVALID";
        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        // when + then
        GenericException exception = assertThrows(GenericException.class, () ->
                parkingLotService.getParkingLotAvailability(lotId)
        );

        assertEquals("PID002", exception.getCode());
        verify(lotRepository, times(1)).findById(lotId);
    }


    @Test
    void testGetParkingLot_Success() {
        // given
        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("L001")
                .location("Manila Central Lot")
                .capacity(100)
                .costPerMinute(2.5)
                .build();

        ResponseParkingLotDTO responseParkingLotDTO = ResponseParkingLotDTO.builder()
                .lotId("L001")
                .location("Manila Central Lot")
                .capacity(100)
                .costPerMinute(2.5)
                .build();


        when(lotRepository.findById("L001")).thenReturn(Optional.of(parkingLot));
        when(modelMapper.map(parkingLot, ResponseParkingLotDTO.class)).thenReturn(responseParkingLotDTO);

        // when
        ResponseParkingLotDTO result = parkingLotService.getParkingLot("L001");

        // then
        assertNotNull(result);
        assertEquals("L001", result.getLotId());
        assertEquals("Manila Central Lot", result.getLocation());
        assertEquals(100, result.getCapacity());
        assertEquals(2.5, result.getCostPerMinute());

        verify(lotRepository, times(1)).findById("L001");
        verify(modelMapper, times(1)).map(parkingLot, ResponseParkingLotDTO.class);
    }

    @Test
    void testGetParkingLot_NotFound_ShouldThrowException() {
        // given
        String invalidId = "L999";
        when(lotRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when + then
        GenericException exception = assertThrows(GenericException.class, () ->
                parkingLotService.getParkingLot(invalidId)
        );

        assertEquals("PID002", exception.getCode());
        verify(lotRepository, times(1)).findById(invalidId);
    }




}
