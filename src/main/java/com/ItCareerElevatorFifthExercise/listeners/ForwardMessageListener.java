package com.ItCareerElevatorFifthExercise.listeners;

import com.ItCareerElevatorFifthExercise.DTOs.receiveMessage.HandleReceiveMessageThroughWebSocketDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.WsForwardMessageDTO;
import com.ItCareerElevatorFifthExercise.util.ServerIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ForwardMessageListener {

    private final ServerIdentity serverIdentity;
    private final SimpMessageSendingOperations messagingTemplate;

    @KafkaListener(
            topics = "${spring.kafka.topic.forward-message:forwardMessage}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "forwardMessageKafkaListenerContainerFactory"
    )
    public void handleMessageForwardMessage(HandleReceiveMessageThroughWebSocketDTO webSocketDTO) {
        log.info("---> Handling message in the Kafka topic.");

        if (webSocketDTO == null || webSocketDTO.getServerInstanceAddress() == null)
            return;

        if (isReceiverWebSocketConnectionLocatedOnCurrentInstance(webSocketDTO.getServerInstanceAddress())) {
            log.info("Delivering message through web socket connection with session id: {}.", webSocketDTO.getSessionId());

            var payload = new WsForwardMessageDTO(
                    webSocketDTO.getContent(),
                    webSocketDTO.getSenderId(),
                    webSocketDTO.getSenderUsername(),
                    webSocketDTO.getSentAt()
            );

            messagingTemplate
                    .convertAndSendToUser(webSocketDTO.getSessionId(), "/topic/messages", payload);
        }
    }

    private boolean isReceiverWebSocketConnectionLocatedOnCurrentInstance(String serverInstanceAddress) {
        return serverInstanceAddress.equals(serverIdentity.getInstanceAddress());
    }
}
