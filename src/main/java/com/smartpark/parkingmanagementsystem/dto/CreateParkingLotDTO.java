package com.smartpark.parkingmanagementsystem.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateParkingLotDTO {

    @NotBlank(message = "VCP001")
    @NotEmpty(message = "VCP002")
    @Size(max = 50, message = "VCP003")
    private String lotId;

    @NotBlank(message = "VCP004")
    @NotEmpty(message = "VCP005")
    private String location;

    @Min(value = 0, message = "VCP006")
    private int capacity;

    @DecimalMin(value = "0.01", inclusive = true, message = "VCP007")
    private double costPerMinute;
}
