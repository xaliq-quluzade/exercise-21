package com.example.livecoding03052024.controller;

import com.example.livecoding03052024.dto.request.AuthRequestDto;
import com.example.livecoding03052024.dto.request.UserRequestDto;
import com.example.livecoding03052024.dto.response.JwtResponseDto;
import com.example.livecoding03052024.service.JWTService;
import com.example.livecoding03052024.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final JWTService jwtService;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;


    @PostMapping(value = "/register")
    public void saveUser(@RequestBody @Valid UserRequestDto userRequest) {
        userService.insert(userRequest);
    }

    @PostMapping("/login")
    public JwtResponseDto authenticateAndGetToken(@RequestBody AuthRequestDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return JwtResponseDto.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }
}

