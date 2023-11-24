package com.example.userservice.service;

import com.example.userservice.dto.request.JwtRequest;
import com.example.userservice.dto.request.RegistrationRequest;
import com.example.userservice.dto.response.JwtResponse;

public interface JwtService {

    String generateAccessToken(String request);

    String generateRefreshToken();

    Long getTtlRefresh();

    Long getTtlAccess();

    String getAllClaims(String token);

    boolean validationToken(String token);

    boolean isExpired(String token);
}
