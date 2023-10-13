package com.example.cafemanagementsystem.service.impl;

import com.example.cafemanagementsystem.config.security.CustomerUserDetailsService;
import com.example.cafemanagementsystem.config.security.JwtUtil;
import com.example.cafemanagementsystem.constants.CafeConstants;
import com.example.cafemanagementsystem.model.entity.UserEntity;
import com.example.cafemanagementsystem.repository.UserRepository;
import com.example.cafemanagementsystem.service.UserService;
import com.example.cafemanagementsystem.util.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final CustomerUserDetailsService customerUserDetailsService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, CustomerUserDetailsService customerUserDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.customerUserDetailsService = customerUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup: {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                Optional<UserEntity> userWrapper = userRepository.findByEmail(requestMap.get("email"));
                if (userWrapper.isEmpty()) {
                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successful Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Problem in method signUp: {}", e);
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login: {}", requestMap);
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserEntity().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":"
                            + jwtUtil.generateToken(customerUserDetailsService.getUserEntity().getEmail(),
                            customerUserDetailsService.getUserEntity().getRole()) + "\"", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("\"message\":\"Wait for admin approval.\"", HttpStatus.BAD_REQUEST);
                }
            }


        } catch (Exception ex) {
            log.error("{}", ex);
        }

        return new ResponseEntity<String>("\"message\":\"Bad Credentials.\"", HttpStatus.BAD_REQUEST);

    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password");
    }

    private UserEntity getUserFromMap(Map<String, String> requestMap) {
        final UserEntity user = new UserEntity();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}
