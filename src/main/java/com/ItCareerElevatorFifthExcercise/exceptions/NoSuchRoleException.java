package com.ItCareerElevatorFifthExcercise.exceptions;

public class NoSuchRoleException extends RuntimeException {

    public NoSuchRoleException(String message) {
        super(message);
    }

    public NoSuchRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
