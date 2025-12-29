package com.ItCareerElevatorFifthExercise.DTOs.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendMailMessageDTO {

    private String senderUsername;

    private String receiverEmail;

    private String content;
}
