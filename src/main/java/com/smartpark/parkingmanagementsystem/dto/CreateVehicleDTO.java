package com.smartpark.parkingmanagementsystem.dto;

import com.smartpark.parkingmanagementsystem.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVehicleDTO {

    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "VVC001")
    private String licensePlate;

    @NotBlank(message = "VVC002")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "VVC003")
    private String ownerName;

    @NotNull(message = "VVC004")
    private VehicleType type;

}
