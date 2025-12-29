package com.ItCareerElevatorFifthExercise.DTOs.receiveMessage;

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
