package com.ItCareerElevatorFifthExcercise.DTOs.ws;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class WsMessageDTO {

    private String receiverId; // userId or groupId

    private String message;

    private LocationDTO location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sentAt;
}

//WsMessageDTO(receiverId=Id..., message=message, location=LocationDTO(latitude=42.68988914412422, longitude=23.31403409653567, timestamp=1766576052914), sentAt=2025-12-24T11:34:12.914)