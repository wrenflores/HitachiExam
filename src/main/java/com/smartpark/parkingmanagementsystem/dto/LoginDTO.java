package com.smartpark.parkingmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    @NotBlank(message = "VCP004")
    @NotEmpty(message = "VCP005")
    private String username;

    @NotBlank(message = "VCP004")
    @NotEmpty(message = "VCP005")
    private String password;
}
