package com.ItCareerElevatorFifthExercise.util.ws;

import com.ItCareerElevatorFifthExercise.DTOs.auth.request.MsvcUserPresenceDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExercise.util.ServerIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class WebSocketEventsListener {

    private final UserService userService;
    private final ServerIdentity serverIdentity;

    @EventListener
    public void onConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        String sessionId = accessor.getSessionId();

        String username = principal.getName();
        User loggedInUser = userService.getByUsername(username);

        MsvcUserPresenceDTO requestDTO = new MsvcUserPresenceDTO(
                loggedInUser.getId(),
                username,
                serverIdentity.getInstanceAddress()
        );

        // TODO: Send the request

        System.out.println("Connection from user " + username);
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        String sessionId = accessor.getSessionId();

        String username = principal.getName();
        User loggedInUser = userService.getByUsername(username);

        // TODO: Send a request to delete the entry in the microservice

        System.out.println("User " + username + " disconnected.");
    }
}
