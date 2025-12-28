package com.ItCareerElevatorFifthExercise.DTOs.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HandleReceiveMessageRequestDTO {

    private String sessionId;

    private String content;
}
