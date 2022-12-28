package com.dataart.secondmonth.controller;

import com.dataart.secondmonth.dto.AuthRequestDto;
import com.dataart.secondmonth.dto.JwtResponseDto;
import com.dataart.secondmonth.dto.RegistrationUserDto;
import com.dataart.secondmonth.dto.UserDto;
import com.dataart.secondmonth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("auth")
    public ResponseEntity<JwtResponseDto> authenticate(@Valid @RequestBody AuthRequestDto loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    @PostMapping("registration")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerNewUser(registrationUserDto));
    }

    @PostMapping("confirm-account")
    public ResponseEntity<?> activateAccount(@RequestParam("token") String token) {
        authService.activateAccount(token);

        return ResponseEntity.accepted().build();
    }

}
