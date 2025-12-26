package com.ItCareerElevatorFifthExercise.DTOs.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MsvcUserPresenceDTO {

    private String userId;

    private String username;

    private String serverInstance;
}
