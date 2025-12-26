package com.ItCareerElevatorFifthExercise.DTOs.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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

    private String receiverId; // ? user or group

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sentAt;
}
