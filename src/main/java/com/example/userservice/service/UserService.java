package com.example.userservice.service;

import com.example.userservice.dto.request.JwtRequest;
import com.example.userservice.dto.request.RegistrationRequest;
import com.example.userservice.dto.request.SignOutRequest;
import com.example.userservice.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    JwtResponse registration(RegistrationRequest request);

    ResponseEntity<?> authorization(JwtRequest request);

    JwtResponse updateTokens(String accessToken, String refreshToken);

    void signOut(SignOutRequest request);
}
