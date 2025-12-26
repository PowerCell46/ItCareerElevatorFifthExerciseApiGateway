package com.ItCareerElevatorFifthExercise.DTOs.auth.response;

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
