package com.example.userservice.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.example.userservice.dto.jpa.entity.UserEntity;
import com.example.userservice.dto.jpa.repository.UsersRepository;
import com.example.userservice.dto.request.JwtRequest;
import com.example.userservice.dto.request.RegistrationRequest;
import com.example.userservice.dto.response.JwtResponse;
import com.example.userservice.service.JwtService;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.tokenTtl}")
    private Long ttlRefresh;

    @Value("${jwt.tokenAccessTtl}")
    private Long ttlAccess;

    public Long getTtlRefresh() {
        return ttlRefresh;
    }

    public Long getTtlAccess() {
        return ttlAccess;
    }

    @Override
    public String generateAccessToken(String request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        return JWT.create()
                .withClaim("username", request)
                .withHeader(map)
                .withExpiresAt(LocalDateTime.now().toInstant(ZoneOffset.UTC).plusSeconds(ttlAccess))
                .sign(Algorithm.HMAC256((secret)));
    }

    @Override
    public String generateRefreshToken() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        return JWT.create()
                .withExpiresAt(LocalDateTime.now().toInstant(ZoneOffset.UTC).plusSeconds(ttlRefresh))
                .withHeader(map)
                .sign(Algorithm.HMAC256((secret)));
    }

    @Override
    public String getAllClaims(String token) {
        String str = String.valueOf(JWT.decode(token).getClaim("username"));
        return str.substring(1, str.length() - 1);
    }

    @Override
    public boolean validationToken(String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alg", "HS256");
        String tmpT = JWT.create()
                .withClaim("username", getAllClaims(token))
                .withExpiresAt(JWT.decode(token).getExpiresAt())
                .withHeader(map)
                .sign(Algorithm.HMAC256(secret));
        if (JWT.decode(token).getSignature().equals(tmpT.substring(tmpT.lastIndexOf('.') + 1))) return true;
        else return false;
    }

    @Override
    public boolean validationExpiredToken(String token) {
        if (JWT.decode(token).getExpiresAt().toInstant().compareTo(LocalDateTime.now().toInstant(ZoneOffset.UTC)) > 0) {
            return false;
        }
        return true;
    }

}
