package com.ItCareerElevatorFifthExcercise.services.interfaces;

import com.ItCareerElevatorFifthExcercise.DTOs.ws.WsMessageDTO;

public interface MessageService {

    void sendMessage(WsMessageDTO messageDTO, String loggedInUserUsername);
}
