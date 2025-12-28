package com.ItCareerElevatorFifthExercise.exceptions.auth;

public class EmailIsAlreadyTakenException extends RuntimeException {

    public EmailIsAlreadyTakenException(String message) {
        super(message);
    }

    public EmailIsAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
