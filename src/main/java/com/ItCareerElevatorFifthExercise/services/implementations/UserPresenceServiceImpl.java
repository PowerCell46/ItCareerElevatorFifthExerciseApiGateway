package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.userPresence.MsvcAddUserPresenceDTO;
import com.ItCareerElevatorFifthExercise.DTOs.userPresence.MsvcRemoveUserPresenceRequestDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.exceptions.MessagingMicroserviceException;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserPresenceService;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExercise.util.ServerIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPresenceServiceImpl implements UserPresenceService {

    private final UserService userService;
    private final ServerIdentity serverIdentity;
    private final WebClient userPresenceWebClient;

    @Override
    public void addUserServerWebSocketConnectionInstanceAddress(String username) {
        User loggedInUser = userService.getByUsername(username);

        MsvcAddUserPresenceDTO requestDTO = new MsvcAddUserPresenceDTO(
                loggedInUser.getId(),
                serverIdentity.getInstanceAddress()
        );

        log.info("Making a request to the userPresence microservice.");

        userPresenceWebClient
                .post()
                .uri("/api/userPresence")
                .bodyValue(requestDTO)
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

    @Override
    public void removeUserServerWebSocketConnectionInstanceAddress(String username) {
        User loggedInUser = userService.getByUsername(username);

        MsvcRemoveUserPresenceRequestDTO requestDTO = new MsvcRemoveUserPresenceRequestDTO(loggedInUser.getId());

        userPresenceWebClient
                .method(HttpMethod.DELETE)
                .uri("/api/userPresence")
                .bodyValue(requestDTO)
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
}
