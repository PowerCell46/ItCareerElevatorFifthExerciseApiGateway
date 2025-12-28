package com.ItCareerElevatorFifthExercise.exceptions.auth;

public class UsernameIsAlreadyTakenException extends RuntimeException {

    public UsernameIsAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameIsAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
