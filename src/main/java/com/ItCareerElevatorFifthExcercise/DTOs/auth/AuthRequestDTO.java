package com.ItCareerElevatorFifthExcercise.DTOs.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequestDTO {

    @NotNull(message = "Username is required.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,20}$",
            message = "Username must be 5-20 characters, contain letters and digits, with at least one uppercase letter and one digit."
    )
    private String username;

    @NotNull(message = "Password is required.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "Password must be 8-20 characters, contain letters and digits, with at least one uppercase letter and one digit."
    )
    private String password;
}
