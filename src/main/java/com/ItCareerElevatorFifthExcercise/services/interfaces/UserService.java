package com.ItCareerElevatorFifthExcercise.services.interfaces;

import com.ItCareerElevatorFifthExcercise.DTOs.auth.AssignRolesRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthResponseDTO;
import com.ItCareerElevatorFifthExcercise.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    AuthResponseDTO register(AuthRequestDTO userRequest);

    User save(User user);

    AuthResponseDTO authenticate(String username, String password);

    User getCurrentlyLoggedUser();

    Optional<User> findByUsername(String username);

    User getByUsername(String username);

    void assignRolesToUser(AssignRolesRequestDTO requestDTO);
}
