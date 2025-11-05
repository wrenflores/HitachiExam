package com.smartpark.parkingmanagementsystem.service;

import com.smartpark.parkingmanagementsystem.entity.ParkingLot;
import com.smartpark.parkingmanagementsystem.entity.Vehicle;
import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import com.smartpark.parkingmanagementsystem.repository.VehicleRepository;
import com.smartpark.parkingmanagementsystem.service.impl.AutomaticCheckOutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutomaticCheckOutServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private AutomaticCheckOutServiceImpl automaticCheckOutService;

    private Vehicle expiredVehicle;
    private Vehicle recentVehicle;
    private ParkingLot lot;

    @BeforeEach
    void setUp() {
        lot = ParkingLot.builder()
                .lotId("LOT-001")
                .location("Downtown")
                .capacity(10)
                .costPerMinute(10.0)
                .build();

        expiredVehicle = Vehicle.builder()
                .licensePlate("ABC123")
                .ownerName("John Doe")
                .type(VehicleType.CAR)
                .parkingLot(lot)
                .checkInTime(LocalDateTime.now().minusMinutes(20)) // > 15 min
                .build();

        recentVehicle = Vehicle.builder()
                .licensePlate("XYZ999")
                .ownerName("Jane Smith")
                .type(VehicleType.CAR)
                .parkingLot(lot)
                .checkInTime(LocalDateTime.now().minusMinutes(5)) // < 15 min
                .build();
    }

    @Test
    void testRemoveExpiredVehicles_RemovesOnlyExpiredOnes() {
        // given
        when(vehicleRepository.findByCheckInTimeIsNotNull()).thenReturn(List.of(expiredVehicle, recentVehicle));

        // when
        automaticCheckOutService.removeExpiredVehicles();

        // then
        // Expired vehicle should be reset and saved
        verify(vehicleRepository, times(1)).save(expiredVehicle);
        // Recent one should not be touched
        verify(vehicleRepository, never()).save(recentVehicle);

        // Check reset
        assertNull(expiredVehicle.getCheckInTime());
        assertNull(expiredVehicle.getParkingLot());
    }

    @Test
    void testRemoveExpiredVehicles_NoVehicles() {
        // given
        when(vehicleRepository.findByCheckInTimeIsNotNull()).thenReturn(List.of());

        // when
        automaticCheckOutService.removeExpiredVehicles();

        // then
        verify(vehicleRepository, never()).save(any());
    }


    @Test
    void testRemoveExpiredVehicles_AllRecent() {
        // given
        when(vehicleRepository.findByCheckInTimeIsNotNull()).thenReturn(List.of(recentVehicle));

        // when
        automaticCheckOutService.removeExpiredVehicles();

        // then
        verify(vehicleRepository, never()).save(any());
    }

}
