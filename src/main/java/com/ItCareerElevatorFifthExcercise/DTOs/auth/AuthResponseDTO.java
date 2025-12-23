package com.ItCareerElevatorFifthExcercise.DTOs.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {

    private String username;

    private String token;
}
