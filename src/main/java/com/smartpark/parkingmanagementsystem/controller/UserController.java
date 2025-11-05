package com.smartpark.parkingmanagementsystem.controller;

import com.smartpark.parkingmanagementsystem.dto.LoginDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseUserDTO;
import com.smartpark.parkingmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseUserDTO> Login(@Valid @RequestBody LoginDTO loginDTO){

        return ResponseEntity.ok(userService.verifyUser(loginDTO));
    }
}
