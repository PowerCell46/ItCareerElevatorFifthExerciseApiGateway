package com.ItCareerElevatorFifthExcercise.DTOs.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MsvcMessageRequestDTO {

    @JsonProperty("senderId")
    private String userId;

    @JsonProperty("senderUsername")
    private String username;

    @JsonProperty("senderEmail")
    private String email;

    @JsonProperty("senderLocation")
    private MsvcLocationRequestDTO location;

    private String receiverId; // user or group

    private String message;
}
