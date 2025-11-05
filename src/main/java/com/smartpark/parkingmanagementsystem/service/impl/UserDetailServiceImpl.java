package com.smartpark.parkingmanagementsystem.service.impl;

import com.smartpark.parkingmanagementsystem.dto.UserPrincipal;
import com.smartpark.parkingmanagementsystem.entity.Users;
import com.smartpark.parkingmanagementsystem.exception.ErrorConstants;
import com.smartpark.parkingmanagementsystem.exception.GenericException;
import com.smartpark.parkingmanagementsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws GenericException {
        Users user = userRepository.findByUsername(username);

        if(user == null){
            String errorCode = "USR001";
            log.error("Exception occurred. {} - {}", errorCode, ErrorConstants.getMessage(errorCode));
            throw new GenericException(errorCode, ErrorConstants.getMessage(errorCode));
        }

        return UserPrincipal.builder()
                .users(user)
                .build();
    }
}
