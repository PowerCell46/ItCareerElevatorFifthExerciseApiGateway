package com.ItCareerElevatorFifthExercise.DTOs.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MsvcLocationRequestDTO {

    private Double latitude;

    private Double longitude;

    private Long timestamp;
}
