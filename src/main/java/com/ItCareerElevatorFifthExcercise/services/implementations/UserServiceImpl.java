package com.ItCareerElevatorFifthExcercise.services.implementations;

import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthRequestDTO;
import com.ItCareerElevatorFifthExcercise.DTOs.auth.AuthResponseDTO;
import com.ItCareerElevatorFifthExcercise.entities.User;
import com.ItCareerElevatorFifthExcercise.exceptions.InvalidCredentialsException;
import com.ItCareerElevatorFifthExcercise.exceptions.UserAlreadyExistsException;
import com.ItCareerElevatorFifthExcercise.repositories.UserRepository;
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
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public UserServiceImpl(
            JwtUtils jwtUtils,
            @Lazy AuthenticationManager authenticationManager,
            PasswordEncoder encoder, UserRepository userRepository
    ) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public AuthResponseDTO register(AuthRequestDTO userRequest) { // ? Cache the users in Redis? (API gateway has to be as FAST as possible!)
        if (findByUsername(userRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(
                    String.format("User with username %s already exists.", userRequest.getUsername())
            );
        }

        User user = constructNonPersistedUser(userRequest);
        user = save(user);

        return authenticate(user.getUsername(), userRequest.getPassword());
    }

    private User constructNonPersistedUser(AuthRequestDTO userRequest) {
        String encodedPassword = encoder.encode(userRequest.getPassword());

        return new User(userRequest.getUsername(), encodedPassword);
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

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found."));

        return new CustomUserDetails(user);
    }
}
