package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.message.MsvcLocationRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.message.MsvcMessageRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.HandleReceiveMessageRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.WsMessageDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.exceptions.msvc.MessagingMicroserviceException;
import com.ItCareerElevatorFifthExercise.services.interfaces.MessageService;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExercise.util.RetryPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${app.kafka.topics.mail-send-message}")
    private String MAIL_SEND_MESSAGE_TOPIC_NAME;

    private final UserService userService;
    private final WebClient messagingWebClient;
    private final KafkaTemplate<String, String> emailKafkaTemplate;

    @Override
    public void sendMessage(WsMessageDTO messageDTO, String loggedInUserUsername) {
        log.info("Making a request to the message microservice.");

        messagingWebClient
                .post()
                .uri("/api/messages")
                .bodyValue(constructMsvcMessageRequestDTO(messageDTO, loggedInUserUsername))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        resp -> resp
                                .bodyToMono(ErrorResponseDTO.class)
                                .map(MessagingMicroserviceException::new)
                                .flatMap(Mono::error)
                )
                .toBodilessEntity()
                .retryWhen(buildRetrySpec())
                .block();
    }

    @Override
    public void forwardMessageToEmail(HandleReceiveMessageRequestDTO requestDTO) {
        // Fetch the users
        // Send a message to a kafka topic
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

                    return new MessagingMicroserviceException(error);
                });
    }
}
