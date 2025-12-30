package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.ws.WsMessageDTO;
import com.ItCareerElevatorFifthExercise.OutputMessage;
import com.ItCareerElevatorFifthExercise.services.interfaces.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;

    @SendTo("/topic/messages")
    @MessageMapping("/message")
    public OutputMessage send(@Valid WsMessageDTO message, Principal principal) { // TODO: Change return type
        String username = principal.getName();

        log.info("---> Sent message: {}{}.", System.lineSeparator(), message);
        messageService.sendMessage(message, username);

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(username, message.getContent(), time);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(MethodArgumentNotValidException ex) {
        log.error("Handling validation error in WebSocket message: {}.", ex.getMessage());

        return "Validation error: " + ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
