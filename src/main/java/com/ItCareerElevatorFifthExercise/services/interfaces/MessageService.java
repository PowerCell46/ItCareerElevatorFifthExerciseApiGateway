package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.ws.WsMessageDTO;

public interface MessageService {

    void sendMessage(WsMessageDTO messageDTO, String loggedInUserUsername);
}
