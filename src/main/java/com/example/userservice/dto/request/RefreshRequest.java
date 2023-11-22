package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshRequest {

    private final String accessToken;

    private final String refreshToken;
}
