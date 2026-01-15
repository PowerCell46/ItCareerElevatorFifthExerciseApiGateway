package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.message.ConversationSummaryResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.receiveMessage.HandleReceiveMessageThroughEmailRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.WsMessageDTO;

import java.util.List;

public interface MessageService {

    void sendMessage(WsMessageDTO messageDTO, String loggedInUserUsername);

    void forwardMessageToEmail(HandleReceiveMessageThroughEmailRequestDTO requestDTO);

    List<ConversationSummaryResponseDTO> getUserConversationsLastMessage();
}
