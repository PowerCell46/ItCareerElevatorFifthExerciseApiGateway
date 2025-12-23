package com.ItCareerElevatorFifthExcercise.DTOs.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserResponseDTO {

    private String username;

    private String email;
}
