package com.ItCareerElevatorFifthExcercise.controllers;

import com.ItCareerElevatorFifthExcercise.DTOs.ws.WsMessageDTO;
import com.ItCareerElevatorFifthExcercise.OutputMessage;
import com.ItCareerElevatorFifthExcercise.services.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;

    @SendTo("/topic/messages")
    @MessageMapping("/message")
    public OutputMessage send(WsMessageDTO message, Principal principal) {
        String username = principal.getName();

        log.info("---> Received message: {}{}", System.lineSeparator(), message);
        messageService.sendMessage(message, username);

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(username, message.getMessage(), time);
    }
}
