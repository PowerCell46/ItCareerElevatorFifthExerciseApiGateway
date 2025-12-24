package com.ItCareerElevatorFifthExcercise.DTOs.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MessageDTO {

    private String from;

    private String receiverId; // userId or groupId

    private String message;

    private LocationDTO location;
}
