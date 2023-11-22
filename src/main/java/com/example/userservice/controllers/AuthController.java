package com.example.userservice.controllers;

import com.example.userservice.dto.request.JwtRequest;
import com.example.userservice.dto.request.RefreshRequest;
import com.example.userservice.dto.request.RegistrationRequest;
import com.example.userservice.dto.request.SignOutRequest;
import com.example.userservice.dto.response.JwtResponse;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody @Valid RegistrationRequest request){
        if(!request.getPassword().equals(request.getConfirmPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный ввод паролей");
        }
        return ResponseEntity.ok(userService.registration(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authorisation(@RequestBody @Valid JwtRequest request){
        return ResponseEntity.ok(userService.authorization(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshTokens(@RequestBody @Valid RefreshRequest request){
        return ResponseEntity.ok(userService.updateTokens(request.getAccessToken(), request.getRefreshToken()));
    }

    @PostMapping("/signout")
    public void signOut(@RequestBody @Valid SignOutRequest request){
        userService.signOut(request);
    }

}
