package com.example.userservice.service.impl;

import com.example.userservice.dto.jpa.entity.UserEntity;
import com.example.userservice.dto.jpa.repository.UsersRepository;
import com.example.userservice.dto.request.JwtRequest;
import com.example.userservice.dto.request.RegistrationRequest;
import com.example.userservice.dto.request.SignOutRequest;
import com.example.userservice.dto.response.JwtResponse;
import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public JwtResponse registration(RegistrationRequest request) {
        if (usersRepository.findUserEntityByUsername(request.getUsername()) != null){
            return null;
        }
        JwtResponse response = JwtResponse.builder()
                .refreshToken(jwtService.generateRefreshToken())
                .accessToken(jwtService.generateAccessToken(request.getUsername()))
                .ttl(jwtService.getTtlAccess())
                .build();
        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .refreshToken(response.getRefreshToken())
                .build();
        usersRepository.save(userEntity);
        return response;
    }

    @Override
    public ResponseEntity<?> authorization(JwtRequest request) {
        UserEntity user = usersRepository.findUserEntityByUsername(request.getUsername());
        if(user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())){
            user.setRefreshToken(jwtService.generateRefreshToken());
            usersRepository.updateByUsername(user.getRefreshToken(), user.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwtService.generateAccessToken(request.getUsername()), user.getRefreshToken(), jwtService.getTtlAccess()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный ввод данных");
    }

    @Override
    public JwtResponse updateTokens(String accessToken, String refreshToken) {
        JwtResponse response = null;
        UserEntity userEntity = usersRepository.findUserEntityByUsername(jwtService.getAllClaims(accessToken));
        if(
                jwtService.validationToken(accessToken)
                && userEntity != null
                && userEntity.getRefreshToken().equals(refreshToken)){

            accessToken = jwtService.generateAccessToken(jwtService.getAllClaims(accessToken));
            refreshToken = jwtService.generateRefreshToken();
            userEntity.setRefreshToken(refreshToken);
            usersRepository.updateByUsername(refreshToken, jwtService.getAllClaims(accessToken));
            response = new JwtResponse(accessToken, refreshToken, jwtService.getTtlAccess());

        }
        return response;
    }

    @Override
    public void signOut(SignOutRequest request) {
        if (!jwtService.validationToken(request.getAccessToken())) return;
        UserEntity userEntity = usersRepository.findUserEntityByUsername(jwtService.getAllClaims(request.getAccessToken()));
        userEntity.setRefreshToken("");
        usersRepository.updateByUsername("",jwtService.getAllClaims(request.getAccessToken()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepository.findUserEntityByUsername(username);
        return new User(user.getUsername(), user.getPassword(), null);
    }



}
