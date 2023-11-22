package com.example.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class SignOutRequest {
    private String accessToken;
}
