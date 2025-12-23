package com.ItCareerElevatorFifthExcercise.exceptions;

public class EmailIsAlreadyTakenException extends RuntimeException {

    public EmailIsAlreadyTakenException(String message) {
        super(message);
    }

    public EmailIsAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
