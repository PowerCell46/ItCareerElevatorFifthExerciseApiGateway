package com.ItCareerElevatorFifthExercise.listeners;

import com.ItCareerElevatorFifthExercise.DTOs.receiveMessage.HandleReceiveMessageThroughWebSocketDTO;
import com.ItCareerElevatorFifthExercise.DTOs.ws.WsForwardMessageDTO;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
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

    private final UserService userService;
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

        if (!isReceiverWebSocketConnectionLocatedOnCurrentInstance(webSocketDTO.getServerInstanceAddress()))
            return;

        String receiverUsername = resolveReceiverUsername(webSocketDTO.getReceiverId());
        if (receiverUsername == null)
            return;

        log.info("Delivering message to receiver {} via /user/queue/messages.", receiverUsername);

        var payload = new WsForwardMessageDTO(
                webSocketDTO.getContent(),
                webSocketDTO.getSenderId(),
                webSocketDTO.getSenderUsername(),
                webSocketDTO.getSentAt()
        );

        messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/messages", payload);
    }

    private String resolveReceiverUsername(String receiverId) {
        if (receiverId == null)
            return null;

        try {
            return userService.getById(receiverId).getUsername();

        } catch (Exception e) {
            log.warn("Could not resolve receiverId to username: {}", receiverId, e);
            return null;
        }
    }

    private boolean isReceiverWebSocketConnectionLocatedOnCurrentInstance(String serverInstanceAddress) {
        return serverInstanceAddress.equals(serverIdentity.getInstanceAddress());
    }
}
