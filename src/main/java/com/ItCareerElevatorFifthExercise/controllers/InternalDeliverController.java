package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.ws.HandleReceiveMessageRequestDTO;
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
public class InternalDeliverController { // * Handle messages on the receiver's part

    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/deliverMessage")
    public void deliverMessageToRecipient(@RequestBody HandleReceiveMessageRequestDTO request) {
        log.info("Delivering message to session id: {}.", request.getSessionId());

        messagingTemplate
                .convertAndSendToUser(
                        request.getSessionId(),
                        "/topic/messages",
                        request.getContent()
                );
    }
}
