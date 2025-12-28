package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.DTOs.auth.request.AssignRolesRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.request.PatchUserRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.request.RegisterRequestDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.response.AuthResponseDTO;
import com.ItCareerElevatorFifthExercise.DTOs.auth.response.AlterUserResponseDTO;
import com.ItCareerElevatorFifthExercise.entities.Role;
import com.ItCareerElevatorFifthExercise.entities.User;
import com.ItCareerElevatorFifthExercise.exceptions.auth.EmailIsAlreadyTakenException;
import com.ItCareerElevatorFifthExercise.exceptions.auth.InvalidCredentialsException;
import com.ItCareerElevatorFifthExercise.exceptions.auth.NoSuchUserException;
import com.ItCareerElevatorFifthExercise.exceptions.auth.UsernameIsAlreadyTakenException;
import com.ItCareerElevatorFifthExercise.repositories.UserRepository;
import com.ItCareerElevatorFifthExercise.services.interfaces.RoleService;
import com.ItCareerElevatorFifthExercise.services.interfaces.UserService;
import com.ItCareerElevatorFifthExercise.util.auth.CustomUserDetails;
import com.ItCareerElevatorFifthExercise.util.auth.JwtUtils;
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

@Slf4j
@Service
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
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO userRequest) {
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

        // Practically would never happen (it would, if you call from an endpoint method, where you aren't authenticated)
        throw new IllegalStateException("Illegal state: no authenticated user.");
    }

    private Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getByUsername(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new NoSuchUserException(String.format("No user found with username %s.", username)));
    }

    @Override
    public AlterUserResponseDTO assignRolesToUser(AssignRolesRequestDTO requestDTO) {
        User user = getByUsername(requestDTO.getUsername());

        requestDTO
                .getRoles()
                .forEach(roleName -> {
                    Role role = roleService.getByName(roleName);
                    user.getRoles().add(role);
                });
        save(user);

        return new AlterUserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @Override
    public AlterUserResponseDTO update(User user, PatchUserRequestDTO userRequest) {
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

        if (userRequest.getPassword() != null) { // ? Normally this would happen by a link sent to the email for resetting the password
            user.setPassword(encodeUserPassword(userRequest.getPassword()));
        }

        if (userRequest.getUsername() != null || userRequest.getEmail() != null || userRequest.getPassword() != null) {
            log.info("Updating user {}.", user.getUsername());
            save(user);
        }

        return new AlterUserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username);

        return new CustomUserDetails(user);
    }
}
