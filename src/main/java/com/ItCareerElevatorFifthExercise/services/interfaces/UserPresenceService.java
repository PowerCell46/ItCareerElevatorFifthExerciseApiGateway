package com.ItCareerElevatorFifthExercise.services.interfaces;

public interface UserPresenceService {

    void addUserServerWebSocketConnectionInstanceAddress(String username, String sessionId);

    void removeUserServerWebSocketConnectionInstanceAddress(String username);
}
