package com.smartpark.parkingmanagementsystem.service;

import com.smartpark.parkingmanagementsystem.dto.LoginDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseUserDTO;

public interface UserService {
    ResponseUserDTO verifyUser(LoginDTO loginDTO);
}
