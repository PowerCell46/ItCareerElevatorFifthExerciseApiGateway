package com.ItCareerElevatorFifthExcercise.services.interfaces;

import com.ItCareerElevatorFifthExcercise.DTOs.auth.AssignRolesRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.RegisterRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthResponseDTO;
import com.ItCareerElevatorFifthExcercise.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    AuthResponseDTO register(RegisterRequestDTO userRequest);

    User save(User user);

    AuthResponseDTO authenticate(String username, String password);

    User getCurrentlyLoggedUser();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User getByUsername(String username);

    void assignRolesToUser(AssignRolesRequestDTO requestDTO);
}
