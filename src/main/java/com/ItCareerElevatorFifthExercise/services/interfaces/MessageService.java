package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.ws.HandleReceiveMessageThroughEmailRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.HandleReceiveMessageThroughWebSocketRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.WsMessageDTO;

public interface MessageService {

    void sendMessage(WsMessageDTO messageDTO, String loggedInUserUsername);

    void forwardMessageToEmail(HandleReceiveMessageThroughEmailRequestDTO requestDTO);
}
