package com.ItCareerElevatorFifthExercise.services.interfaces;

public interface UserPresenceService {

    void addUserServerWebSocketConnectionInstanceAddress(String username);

    void removeUserServerWebSocketConnectionInstanceAddress(String username);
}
