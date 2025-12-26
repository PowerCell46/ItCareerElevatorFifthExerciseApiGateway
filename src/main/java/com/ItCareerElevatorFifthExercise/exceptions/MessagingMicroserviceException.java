package com.ItCareerElevatorFifthExercise.exceptions;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import lombok.Getter;

@Getter
public class MessagingMicroserviceException extends RuntimeException {

    private final Integer status;

    private final Long timestamp;

    public MessagingMicroserviceException(ErrorResponseDTO errorResponseDTO) {
        super(errorResponseDTO.getMessage());
        this.status = errorResponseDTO.getStatus();
        this.timestamp = errorResponseDTO.getTimestamp();
    }
}
