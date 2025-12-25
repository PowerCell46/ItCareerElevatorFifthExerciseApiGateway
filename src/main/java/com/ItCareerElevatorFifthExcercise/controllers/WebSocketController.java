package com.ItCareerElevatorFifthExcercise.controllers;

import com.ItCareerElevatorFifthExcercise.DTOs.ws.WsMessageDTO;
import com.ItCareerElevatorFifthExcercise.OutputMessage;
import com.ItCareerElevatorFifthExcercise.services.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public OutputMessage send(WsMessageDTO message, Principal principal) {
        String username = principal.getName();

        System.out.println("___||___" + message);
        messageService.sendMessage(message, username);

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(username, message.getMessage(), time);
    }
}
