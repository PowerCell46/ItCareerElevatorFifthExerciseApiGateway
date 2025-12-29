package com.ItCareerElevatorFifthExercise.DTOs.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HandleReceiveMessageThroughEmailRequestDTO {

    private String senderId;

    private String receiverId;

    private String content;
}
