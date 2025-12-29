package com.ItCareerElevatorFifthExercise.DTOs.receiveMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HandleReceiveMessageThroughWebSocketRequestDTO {

    private String sessionId;

    private String content;
}
