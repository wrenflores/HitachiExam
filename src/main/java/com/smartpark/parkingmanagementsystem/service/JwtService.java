package com.smartpark.parkingmanagementsystem.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(String username);

    String getUsernameFromJwt(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
