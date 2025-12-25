package com.ItCareerElevatorFifthExcercise.services.implementations;

import com.ItCareerElevatorFifthExcercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.message.MsvcLocationRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.message.MsvcMessageRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.ws.WsMessageDTO;
import com.ItCareerElevatorFifthExcercise.entities.User;
import com.ItCareerElevatorFifthExcercise.exceptions.MessagingMicroserviceException;
import com.ItCareerElevatorFifthExcercise.services.interfaces.MessageService;
import com.ItCareerElevatorFifthExcercise.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserService userService;
    private final WebClient messagingWebClient;

    @Override
    public void sendMessage(WsMessageDTO messageDTO, String loggedInUserUsername) {
        log.info("Making a request to the orders microservice.");

        messagingWebClient
                .post()
                .uri("/api/messages")
                .bodyValue(constructMsvcMessageRequestDTO(messageDTO, loggedInUserUsername))
                .retrieve()
                .onStatus(HttpStatusCode::isError, // TODO: Look for a better approach (test all possible custom errors)
                        resp -> resp
                                .bodyToMono(ErrorResponseDTO.class)
                                .map(MessagingMicroserviceException::new)
                                .flatMap(Mono::error)
                )
//                .bodyToMono(OrderResponseDTO.class)
                .toBodilessEntity()
                .block();
    }

    private MsvcMessageRequestDTO constructMsvcMessageRequestDTO(WsMessageDTO messageDTO, String loggedInUserUsername) {
        User loggedInUser = userService.getByUsername(loggedInUserUsername);

        return new MsvcMessageRequestDTO(
                loggedInUser.getId(),
                loggedInUserUsername,
                loggedInUser.getEmail(),
                new MsvcLocationRequestDTO(
                        messageDTO.getLocation().getLatitude(),
                        messageDTO.getLocation().getLongitude(),
                        messageDTO.getLocation().getTimestamp()
                ),
                messageDTO.getReceiverId(),
                messageDTO.getMessage()
        );
    }
}
