package com.ItCareerElevatorFifthExercise.DTOs.userPresence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MsvcAddUserPresenceDTO {

    private String userId;

    private String serverInstanceAddress;

    private String sessionId;
}
