package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.receiveMessage.HandleReceiveMessageThroughEmailRequestDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/deliverMessageToReceiverThroughEmail")
    public void deliverMessageToReceiverThroughEmail(@RequestBody HandleReceiveMessageThroughEmailRequestDTO requestDTO) {
        log.info("Delivering message through email with receiverId: {}.", requestDTO.getReceiverId());

        messageService.forwardMessageToEmail(requestDTO);
    }
}
