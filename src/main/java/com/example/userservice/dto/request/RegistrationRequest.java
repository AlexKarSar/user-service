package com.example.userservice.dto.request;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;

    private String name;

    private String password;

    private String confirmPassword;
}
