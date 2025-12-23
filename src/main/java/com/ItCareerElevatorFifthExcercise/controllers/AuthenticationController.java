package com.ItCareerElevatorFifthExcercise.controllers;

import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthResponseDTO;
import com.ItCareerElevatorFifthExcercise.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRequestDTO userRequest) {
        log.info("---> POST request on api/auth/register with username: {}.", userRequest.getUsername());

        var responseDTO = userService.register(userRequest);

        return ResponseEntity.created(null).body(responseDTO); // TODO: URL
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody AuthRequestDTO userRequest) {
        log.info("---> POST request on api/auth/login with username: {}.", userRequest.getUsername());

        var responseDTO = userService
                .authenticate(userRequest.getUsername(), userRequest.getPassword());

        return ResponseEntity.ok(responseDTO);
    }
}
