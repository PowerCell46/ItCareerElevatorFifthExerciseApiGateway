package com.ItCareerElevatorFifthExcercise.controllers;

import com.ItCareerElevatorFifthExcercise.DTOs.ws.MessageDTO;
import com.ItCareerElevatorFifthExcercise.OutputMessage;
import com.ItCareerElevatorFifthExcercise.entities.User;
import com.ItCareerElevatorFifthExcercise.services.interfaces.UserService;
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

    private final UserService userService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public OutputMessage send(MessageDTO message, Principal principal) {
        String username = principal.getName();

        User user = userService.getByUsername(username);

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(username, message.getMessage(), time);
    }
}
