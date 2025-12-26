package com.ItCareerElevatorFifthExercise.DTOs.ws;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LocationDTO {

    private Double latitude;

    private Double longitude;

    private Long timestamp;
}
