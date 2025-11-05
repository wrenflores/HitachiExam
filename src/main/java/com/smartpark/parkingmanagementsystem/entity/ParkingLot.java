package com.smartpark.parkingmanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_lots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    @Id
    @Column(name = "lot_id", length = 50, nullable = false, unique = true)
    private String lotId;

    @NotBlank
    @Column(nullable = false)
    private String location;

    @Min(0)
    @Column(nullable = false)
    private int capacity;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "cost_per_minute", nullable = false)
    private double costPerMinute;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();

    public int getOccupiedSpaces() {
        return vehicles != null ? vehicles.size() : 0;
    }

    public int getAvailableSpaces() {
        return capacity - getOccupiedSpaces();
    }

    public boolean isFull() {
        return getOccupiedSpaces() >= capacity;
    }
}
