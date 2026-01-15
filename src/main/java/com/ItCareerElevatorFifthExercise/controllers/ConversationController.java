package com.ItCareerElevatorFifthExercise.controllers;

import com.ItCareerElevatorFifthExercise.DTOs.message.ConversationSummaryResponseDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversation")
public class ConversationController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<ConversationSummaryResponseDTO>> getUserConversationsLastMessage() {
        log.info("GET request on /api/conversation.");

        var responseDTO = messageService.getUserConversationsLastMessage();

        return ResponseEntity.ok(responseDTO);
    }
}
