package com.example.userservice.service;

import com.example.userservice.dto.request.JwtRequest;
import com.example.userservice.dto.request.RegistrationRequest;
import com.example.userservice.dto.request.SignOutRequest;
import com.example.userservice.dto.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    JwtResponse registration(RegistrationRequest request);
    ResponseEntity<?> authorization(JwtRequest request);
    JwtResponse updateTokens(String accessToken, String refreshToken);
    void signOut(SignOutRequest request);
}
