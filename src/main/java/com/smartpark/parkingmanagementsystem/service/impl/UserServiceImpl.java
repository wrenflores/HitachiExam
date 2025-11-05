package com.smartpark.parkingmanagementsystem.service.impl;

import com.smartpark.parkingmanagementsystem.dto.LoginDTO;
import com.smartpark.parkingmanagementsystem.dto.ResponseUserDTO;
import com.smartpark.parkingmanagementsystem.exception.ErrorConstants;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import com.smartpark.parkingmanagementsystem.service.JwtService;
import com.smartpark.parkingmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @Override
    public ResponseUserDTO verifyUser(LoginDTO loginDTO) throws GenericException {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()
                        )
                );
        if(authentication.isAuthenticated()){
            ;
            return ResponseUserDTO.builder()
                    .token(jwtService.generateToken(loginDTO.getUsername()))
                    .build();
        }

        String errorCode = "LGN001";
        throw new GenericException(errorCode, ErrorConstants.getMessage(errorCode));
    }
}
