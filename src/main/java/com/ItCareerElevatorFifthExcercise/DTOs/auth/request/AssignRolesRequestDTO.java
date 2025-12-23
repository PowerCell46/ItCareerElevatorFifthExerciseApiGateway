package com.ItCareerElevatorFifthExcercise.DTOs.auth.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AssignRolesRequestDTO {

    @NotNull(message = "Username is required.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{5,20}$",
            message = "Username must be 5-20 characters, contain letters and digits, with at least one uppercase letter and one digit."
    )
    private String username;

    private @NotNull(message = "Roles must not be null.")
    @NotEmpty(message = "At least one role is required.")
    List<@Pattern(regexp = "^[A-Z_]{2,100}$", message = "Role names must be 2–100 characters long and contain only uppercase letters and underscores.") String> roles;
}
