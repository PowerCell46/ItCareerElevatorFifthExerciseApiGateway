package com.ItCareerElevatorFifthExcercise.controllers;

import com.ItCareerElevatorFifthExcercise.DTOs.ws.MessageDTO;
import com.ItCareerElevatorFifthExcercise.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    @MessageMapping("/message") // When a client sends to a destination starting with /ws, route it to @MessageMapping methods
    @SendTo("/topic/messages") // That means the server will forward the return value to /topic/messages.
    public OutputMessage send(MessageDTO message) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println("-----: " + message);

        return new OutputMessage(message.getFrom(), message.getMessage(), time);
    }
}
