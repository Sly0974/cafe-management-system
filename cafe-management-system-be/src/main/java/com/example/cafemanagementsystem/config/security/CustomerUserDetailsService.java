package com.example.cafemanagementsystem.config.security;

import com.example.cafemanagementsystem.model.entity.UserEntity;
import com.example.cafemanagementsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private UserEntity userEntity;

    @Autowired
    public CustomerUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername: {}", username);
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(username);
        if (userEntityWrapper.isPresent()) {
            userEntity = userEntityWrapper.get();
            return new User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
        }

        return null;
    }

    public UserEntity get
}
