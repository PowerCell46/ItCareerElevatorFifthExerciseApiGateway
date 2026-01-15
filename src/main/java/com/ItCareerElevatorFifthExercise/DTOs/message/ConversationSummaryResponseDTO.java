package com.ItCareerElevatorFifthExercise.DTOs.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ConversationSummaryResponseDTO {

    private String senderId;

    private String receiverId;

    private String content;

    private LocalDateTime createdAt;
}
