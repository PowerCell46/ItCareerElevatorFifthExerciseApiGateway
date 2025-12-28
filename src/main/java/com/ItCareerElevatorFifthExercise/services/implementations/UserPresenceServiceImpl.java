package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.userPresence.MsvcAddUserPresenceDTO;
import com.ItCareerElevatorFifthExercise.DTOs.userPresence.MsvcRemoveUserPresenceRequestDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.exceptions.msvc.UserPresenceMicroserviceException;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserPresenceService;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExercise.util.RetryPolicy;
import com.ItCareerElevatorFifthExercise.util.ServerIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPresenceServiceImpl implements UserPresenceService {

    private final UserService userService;
    private final ServerIdentity serverIdentity;
    private final WebClient userPresenceWebClient;

    @Override
    public void addUserServerWebSocketConnectionInstanceAndSessionAddress(String username, String sessionId) {
        User loggedInUser = userService.getByUsername(username);

        MsvcAddUserPresenceDTO requestDTO = new MsvcAddUserPresenceDTO(
                loggedInUser.getId(),
                serverIdentity.getInstanceAddress(),
                sessionId
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
                                .map(UserPresenceMicroserviceException::new)
                                .flatMap(Mono::error)
                )
                .toBodilessEntity()
                .retryWhen(buildRetrySpec())
                .block();
    }

    @Override
    public void removeUserServerWebSocketConnectionInstanceAndSessionAddress(String username) {
        User loggedInUser = userService.getByUsername(username);

        MsvcRemoveUserPresenceRequestDTO requestDTO = new MsvcRemoveUserPresenceRequestDTO(
                loggedInUser.getId()
        );

        log.info("Making a request to the userPresence microservice.");

        userPresenceWebClient
                .method(HttpMethod.DELETE)
                .uri("/api/userPresence")
                .bodyValue(requestDTO)
                .retrieve()
                .onStatus(HttpStatusCode::isError, // TODO: Look for a better approach (test all possible custom errors)
                        resp -> resp
                                .bodyToMono(ErrorResponseDTO.class)
                                .map(UserPresenceMicroserviceException::new)
                                .flatMap(Mono::error)
                )
                .toBodilessEntity()
                .retryWhen(buildRetrySpec())
                .block();
    }

    private Retry buildRetrySpec() {
        return Retry
                .backoff(4, Duration.ofSeconds(2)) // 2s, 4s, 8s, 16s
                .maxBackoff(Duration.ofSeconds(20))
                .jitter(0.5d) // 50% jitter
                .filter(RetryPolicy::isRetriable)
                .onRetryExhaustedThrow((spec, signal) -> {
                    Throwable failure = signal.failure();

                    ErrorResponseDTO error = new ErrorResponseDTO(
                            500,
                            failure.getMessage() != null ? failure.getMessage() : "Internal server error occurred.",
                            System.currentTimeMillis()
                    );

                    return new UserPresenceMicroserviceException(error);
                });
    }
}
