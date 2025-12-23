package com.ItCareerElevatorFifthExcercise.services.interfaces;

import com.ItCareerElevatorFifthExcercise.DTOs.auth.request.AssignRolesRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.request.PatchUserRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.request.RegisterRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.response.AuthResponseDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.response.PatchUserResponseDTO;
import com.ItCareerElevatorFifthExcercise.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    AuthResponseDTO register(RegisterRequestDTO userRequest);

    User save(User user);

    AuthResponseDTO authenticate(String username, String password);

    User getCurrentlyLoggedUser();

    void assignRolesToUser(AssignRolesRequestDTO requestDTO);

    PatchUserResponseDTO update(User user, PatchUserRequestDTO userRequest);
}
