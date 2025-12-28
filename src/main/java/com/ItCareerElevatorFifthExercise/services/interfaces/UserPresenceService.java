package com.ItCareerElevatorFifthExercise.services.interfaces;

public interface UserPresenceService {

    void addUserServerWebSocketConnectionInstanceAndSessionAddress(String username, String sessionId);

    void removeUserServerWebSocketConnectionInstanceAndSessionAddress(String username);
}
