package com.ItCareerElevatorFifthExercise.exceptions;

public class UsernameIsAlreadyTakenException extends RuntimeException {

    public UsernameIsAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameIsAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
