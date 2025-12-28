package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.message.MsvcLocationRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.message.MsvcMessageRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.WsMessageDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.exceptions.msvc.MessagingMicroserviceException;
import com.ItCareerElevatorFifthExercise.services.interfaces.MessageService;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
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
        log.info("Making a request to the message microservice.");

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
//                .bodyToMono(SomeClass.class)
                .toBodilessEntity()
                .block();
    }

    private MsvcMessageRequestDTO constructMsvcMessageRequestDTO(WsMessageDTO messageDTO, String loggedInUserUsername) {
        User loggedInUser = userService.getByUsername(loggedInUserUsername);

        return new MsvcMessageRequestDTO(
                loggedInUser.getId(),
                loggedInUserUsername,
                new MsvcLocationRequestDTO(
                        messageDTO.getLocation().getLatitude(),
                        messageDTO.getLocation().getLongitude(),
                        messageDTO.getLocation().getRecordedAt()
                ),
                messageDTO.getReceiverId(),
                messageDTO.getContent(),
                messageDTO.getSentAt()
        );
    }
}
