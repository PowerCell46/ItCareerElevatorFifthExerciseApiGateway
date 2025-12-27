package com.ItCareerElevatorFifthExercise.DTOs.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReceiveMessageRequestDTO {

    private String sessionId;

    private String content;
}
