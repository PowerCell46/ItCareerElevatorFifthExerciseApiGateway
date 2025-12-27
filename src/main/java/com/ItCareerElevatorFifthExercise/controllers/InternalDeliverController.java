package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.ws.ReceiveMessageRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalDeliverController {

    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/deliver")
    public void deliver(@RequestBody ReceiveMessageRequestDTO request) {
        log.info("Delivering message to session id: {}.", request.getSessionId());

        messagingTemplate
                .convertAndSendToUser(
                        request.getSessionId(),
                        "/topic/messages", // Not sure if it's correct
                        request.getContent()
                );
    }
}
