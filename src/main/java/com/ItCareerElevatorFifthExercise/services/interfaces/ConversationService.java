package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.message.ConversationSummaryResponseDTO;

import java.util.List;

public interface ConversationService {

    List<ConversationSummaryResponseDTO> getUserConversationsLastMessage();
}
