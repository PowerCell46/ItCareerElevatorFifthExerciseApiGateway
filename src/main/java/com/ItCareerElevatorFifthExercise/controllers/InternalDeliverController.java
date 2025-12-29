package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.ws.HandleReceiveMessageRequestDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.MessageService;
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

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping("/deliverMessageToReceiverThroughWebSocket")
    public void deliverMessageToReceiverThroughWebSocket(@RequestBody HandleReceiveMessageRequestDTO requestDTO) {
        log.info("Delivering message through WS connection with session id: {}.", requestDTO.getSessionId());

        messagingTemplate
                .convertAndSendToUser(requestDTO.getSessionId(), "/topic/messages", requestDTO.getContent());
    }

    @PostMapping("/deliverMessageToReceiverThroughEmail")
    public void deliverMessageToReceiverThroughEmail(@RequestBody HandleReceiveMessageRequestDTO requestDTO) {
        log.info("Delivering message through email with receiverId: {}.", requestDTO.getSessionId());

        messageService.forwardMessageToEmail(requestDTO);
    }
}
