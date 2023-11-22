package com.example.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    private final String accessToken;
    private final String refreshToken;
    private final Long ttl;
    public JwtResponse(String accessToken, String refreshToken, Long ttl){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.ttl = ttl;
    }
}
