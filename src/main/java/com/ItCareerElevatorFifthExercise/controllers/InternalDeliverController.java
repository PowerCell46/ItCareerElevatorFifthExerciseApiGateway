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

    @PostMapping("/deliverMessageToReceiverThroughWebSocket")
    public void deliverMessageToReceiverThroughWebSocket(@RequestBody HandleReceiveMessageRequestDTO request) {
        log.info("Delivering message through WS connection with session id: {}.", request.getSessionId());

        messagingTemplate
                .convertAndSendToUser(
                        request.getSessionId(),
                        "/topic/messages",
                        request.getContent()
                );
    }

    @PostMapping("/deliverMessageToReceiverThroughEmail")
    public void deliverMessageToReceiverThroughEmail(@RequestBody HandleReceiveMessageRequestDTO request) {
        log.info("Delivering message through email with receiverId: {}.", request.getSessionId());

        messagingTemplate
                .convertAndSendToUser(
                        request.getSessionId(),
                        "/topic/messages",
                        request.getContent()
                );
    }
}
