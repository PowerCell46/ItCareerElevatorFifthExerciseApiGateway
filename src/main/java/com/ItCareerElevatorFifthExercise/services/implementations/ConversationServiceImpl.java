package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.message.ConversationSummaryResponseDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.exceptions.msvc.MessagePersistenceMicroserviceException;
import com.ItCareerElevatorFifthExercise.services.interfaces.ConversationService;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExercise.util.RetryPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final UserService userService;
    private final WebClient messagePersistenceWebClient;

    @Override
    public List<ConversationSummaryResponseDTO> getUserConversationsLastMessage() {
        User loggedInUser = userService.getCurrentlyLoggedUser();

        log.info("Making a request to the message persistence microservice.");

        return messagePersistenceWebClient
                .get()
                .uri("/api/conversations/" + loggedInUser.getId())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp
                                .bodyToMono(ErrorResponseDTO.class)
                                .map(MessagePersistenceMicroserviceException::new)
                                .flatMap(Mono::error)
                )
                .bodyToFlux(ConversationSummaryResponseDTO.class)
                .collectList()
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

                    return new MessagePersistenceMicroserviceException(error);
                });
    }
}
