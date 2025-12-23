package com.ItCareerElevatorFifthExcercise.services.implementations;

import com.ItCareerElevatorFifthExcercise.DTOs.auth.request.AssignRolesRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.request.PatchUserRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.request.RegisterRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.response.AuthResponseDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.response.PatchUserResponseDTO;
import com.ItCareerElevatorFifthExcercise.entities.Role;
import com.ItCareerElevatorFifthExcercise.entities.User;
import com.ItCareerElevatorFifthExcercise.exceptions.EmailIsAlreadyTakenException;
import com.ItCareerElevatorFifthExcercise.exceptions.InvalidCredentialsException;
import com.ItCareerElevatorFifthExcercise.exceptions.NoSuchUserException;
import com.ItCareerElevatorFifthExcercise.exceptions.UsernameIsAlreadyTakenException;
import com.ItCareerElevatorFifthExcercise.repositories.UserRepository;
import com.ItCareerElevatorFifthExcercise.services.interfaces.RoleService;
import com.ItCareerElevatorFifthExcercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExcercise.util.CustomUserDetails;
import com.ItCareerElevatorFifthExcercise.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(
            JwtUtils jwtUtils, RoleService roleService,
            @Lazy AuthenticationManager authenticationManager,
            PasswordEncoder encoder, UserRepository userRepository
    ) {
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO userRequest) { // ? Cache the users in Redis? (API gateway has to be as FAST as possible!)
        validateRegisterData(userRequest);

        User user = new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                encodeUserPassword(userRequest.getPassword())
        );
        user = save(user);

        return authenticate(user.getUsername(), userRequest.getPassword());
    }

    private void validateRegisterData(RegisterRequestDTO userRequest) {
        if (findByUsername(userRequest.getUsername()).isPresent()) {
            throw new UsernameIsAlreadyTakenException(
                    String.format("User with username %s already exists.", userRequest.getUsername())
            );
        }

        if (findByEmail(userRequest.getEmail()).isPresent()) {
            throw new EmailIsAlreadyTakenException(
                    String.format("Email %s is already taken.", userRequest.getEmail())
            );
        }
    }

    private String encodeUserPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public User save(User user) {
        log.info("Persisting user with username {} to the database.", user.getUsername());

        return userRepository.save(user);
    }

    @Override
    public AuthResponseDTO authenticate(String username, String password) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String jwtToken = jwtUtils.generateToken(userDetails.getUsername());

            return new AuthResponseDTO(userDetails.getUsername(), jwtToken);

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }

    @Override
    public User getCurrentlyLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails cud) {
            return cud.getUser();
        }

        throw new IllegalStateException("No authenticated user."); // Practically this would never happen
    }

    private Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private User getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NoSuchUserException(String.format("No user found with username %s.", username)));
    }

    @Override
    public void assignRolesToUser(AssignRolesRequestDTO requestDTO) {
        User user = getByUsername(requestDTO.getUsername());

        requestDTO
                .getRoles()
                .forEach(roleName -> {
                    Role role = roleService.getByName(roleName);
                    user.getRoles().add(role);
                });

        save(user);
    }

    @Override
    public PatchUserResponseDTO update(User user, PatchUserRequestDTO userRequest) {
        if (userRequest.getUsername() != null) {
            if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
                throw new UsernameIsAlreadyTakenException(
                        String.format("User with username %s already exists.", userRequest.getUsername())
                );
            }
            user.setUsername(userRequest.getUsername());
        }

        if (userRequest.getEmail() != null) {
            if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
                throw new EmailIsAlreadyTakenException(
                        String.format("Email %s is already taken.", userRequest.getEmail()));
            }
            user.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPassword() != null)
            user.setPassword(encodeUserPassword(userRequest.getPassword()));

        if (userRequest.getUsername() != null || userRequest.getEmail() != null || userRequest.getPassword() != null) {
            log.info("Updating user {}.", user.getUsername());
            save(user);
        }

        return new PatchUserResponseDTO(user.getUsername(), user.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username);

        return new CustomUserDetails(user);
    }
}
