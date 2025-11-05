package com.smartpark.parkingmanagementsystem.service;

import com.smartpark.parkingmanagementsystem.dto.CreateVehicleDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleCheckoutDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseVehicleDTO;
import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import com.smartpark.parkingmanagementsystem.repository.ParkingLotRepository;
import com.smartpark.parkingmanagementsystem.repository.VehicleRepository;
import com.smartpark.parkingmanagementsystem.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ParkingLotRepository lotRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;


    @Test
    void testCreateVehicle_Success() {
        // given
        CreateVehicleDTO createVehicleDTO = CreateVehicleDTO.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .build();

        // when
        when(vehicleRepository.existsById("ABC123")).thenReturn(false);
        when(modelMapper.map(createVehicleDTO, Vehicle.class)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(modelMapper.map(vehicle, CreateVehicleDTO.class)).thenReturn(createVehicleDTO);


        CreateVehicleDTO result = vehicleService.createVehicle(createVehicleDTO);

        // then
        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        assertEquals("John Doe", result.getOwnerName());
        assertEquals(VehicleType.CAR, result.getType());

        verify(vehicleRepository, times(1)).existsById("ABC123");
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testCreateVehicle_ThrowsGenericException_WhenLicensePlateExists() {
        // given
        CreateVehicleDTO createVehicleDTO = CreateVehicleDTO.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .build();

        when(vehicleRepository.existsById("ABC123")).thenReturn(true);

        // when & then
        GenericException exception = assertThrows(GenericException.class, () ->
                vehicleService.createVehicle(createVehicleDTO)
        );

        assertTrue(exception.getMessage().contains("ABC123"));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testCheckInVehicle_Success() {
        // given
        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .build();

        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .vehicles(new ArrayList<>())
                .build();

        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));
        when(lotRepository.findById("LOT-001")).thenReturn(Optional.of(parkingLot));
        when(modelMapper.map(any(Vehicle.class), eq(ResponseVehicleDTO.class)))
                .thenAnswer(invocation -> {
                    Vehicle v = invocation.getArgument(0);
                    return ResponseVehicleDTO.builder()
                            .licensePlate(v.getLicensePlate())
                            .ownerName(v.getOwnerName())
                            .type(v.getType())
                            .lotId("LOT-001")
                            .build();
                });

        // when
        ResponseVehicleDTO result = vehicleService.checkInVehicle("ABC123", "LOT-001");

        // then
        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        assertEquals("LOT-001", result.getLotId());
        assertEquals(VehicleType.CAR, result.getType());
        assertNotNull(vehicle.getCheckInTime()); // should be set

        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testCheckInVehicle_ThrowsWhenVehicleNotFound() {
        when(vehicleRepository.findById("XYZ999")).thenReturn(Optional.empty());

        GenericException exception = assertThrows(GenericException.class,
                () -> vehicleService.checkInVehicle("XYZ999", "LOT-001")
        );

        assertTrue(exception.getCode().contains("VID002"));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testCheckInVehicle_ThrowsWhenLotIsFull() {
        // given
        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .build();

        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .vehicles(new ArrayList<>())
                .build();

        parkingLot = spy(parkingLot);
        doReturn(true).when(parkingLot).isFull();

        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));
        when(lotRepository.findById("LOT-001")).thenReturn(Optional.of(parkingLot));

        GenericException exception = assertThrows(GenericException.class,
                () -> vehicleService.checkInVehicle("ABC123", "LOT-001")
        );

        assertTrue(exception.getCode().contains("PFL001"));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testCheckOutVehicle_Success() {
        // given
        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .parkingLot(parkingLot)
                .checkInTime(LocalDateTime.now().minusMinutes(10))
                .build();

        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));

        // when
        ResponseVehicleCheckoutDTO result = vehicleService.checkOutVehicle("ABC123");

        // then
        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        assertEquals("LOT-001", result.getLotId());
        assertEquals(VehicleType.CAR, result.getType());
        assertNotNull(result.getCheckInTime());
        assertNotNull(result.getCheckOutTime());
        assertTrue(result.getParkingCost() >= 100); // 10 minutes * 10 per minute
        verify(vehicleRepository, times(1)).save(vehicle);

        // After checkout, vehicle should be reset
        assertNull(vehicle.getCheckInTime());
        assertNull(vehicle.getParkingLot());
    }

    @Test
    void testCheckOutVehicle_ThrowsWhenVehicleNotFound() {
        when(vehicleRepository.findById("XYZ999")).thenReturn(Optional.empty());

        GenericException exception = assertThrows(GenericException.class, () ->
                vehicleService.checkOutVehicle("XYZ999")
        );

        assertTrue(exception.getCode().contains("VID002"));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testCheckOutVehicle_ThrowsWhenNoCheckInTime() {
        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();
        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .parkingLot(parkingLot)
                .checkInTime(null)
                .build();
        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));

        GenericException exception = assertThrows(GenericException.class, () ->
                vehicleService.checkOutVehicle("ABC123")
        );

        assertTrue(exception.getCode().contains("VCI001"));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testCheckOutVehicle_ThrowsWhenNoParkingLot() {

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .parkingLot(null)
                .checkInTime(LocalDateTime.now().minusMinutes(10))
                .build();
        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));

        GenericException exception = assertThrows(GenericException.class, () ->
                vehicleService.checkOutVehicle("ABC123")
        );

        assertTrue(exception.getCode().contains("VCI001"));
        verify(vehicleRepository, never()).save(any());
    }


    @Test
    void testCheckOutVehicle_MinimumOneMinuteCost() {
        ParkingLot parkingLot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown Zone A")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .parkingLot(parkingLot)
                .checkInTime(LocalDateTime.now())
                .build(); // same-minute checkout
        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));

        ResponseVehicleCheckoutDTO result = vehicleService.checkOutVehicle("ABC123");

        assertNotNull(result);
        assertEquals(10.0, result.getParkingCost()); // 1 * 10.0
        verify(vehicleRepository, times(1)).save(vehicle);
    }



}
