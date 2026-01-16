package com.ItCareerElevatorFifthExercise.exceptions.msvc;

import com.ItCareerElevatorFifthExercise.DTOs.common.ErrorResponseDTO;
import lombok.Getter;

@Getter
public class MessagePersistenceMicroserviceException extends RuntimeException {

    private final Integer status;

    private final Long timestamp;

    public MessagePersistenceMicroserviceException(ErrorResponseDTO errorResponseDTO) {
        super(errorResponseDTO.getMessage());
        this.status = errorResponseDTO.getStatus();
        this.timestamp = errorResponseDTO.getTimestamp();
    }
}
