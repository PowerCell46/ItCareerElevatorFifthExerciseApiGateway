package com.ItCareerElevatorFifthExercise.services.interfaces;

import com.ItCareerElevatorFifthExercise.DTOs.auth.request.AssignRolesRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.request.PatchUserRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.request.RegisterRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.response.AuthResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.response.AlterUserResponseDTO;
import com.ItCareerElevatorFifthExercise.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    AuthResponseDTO register(RegisterRequestDTO userRequest);

    User save(User user);

    AuthResponseDTO authenticate(String username, String password);

    User getByUsername(String username);

    User getCurrentlyLoggedUser();

    AlterUserResponseDTO assignRolesToUser(AssignRolesRequestDTO requestDTO);

    AlterUserResponseDTO update(User user, PatchUserRequestDTO userRequest);
}
