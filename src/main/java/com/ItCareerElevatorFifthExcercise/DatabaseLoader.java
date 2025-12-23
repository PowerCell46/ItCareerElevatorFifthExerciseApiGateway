package com.ItCareerElevatorFifthExcercise;

import com.ItCareerElevatorFifthExcercise.entities.Role;
import com.ItCareerElevatorFifthExcercise.entities.User;
import com.ItCareerElevatorFifthExcercise.repositories.RoleRepository;
import com.ItCareerElevatorFifthExcercise.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {

    @Value("${admin.username}")
    private String ADMIN_USERNAME;

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    private static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if (isAdminMissing()) {
            log.info("Initializing the admin user.");

            Role roleAdmin = roleRepository
                    .findByName(ROLE_ADMIN_NAME)
                    .orElse(roleRepository.save(new Role(ROLE_ADMIN_NAME)));

            User adminUser = new User(ADMIN_USERNAME, ADMIN_PASSWORD, Set.of(roleAdmin));
            userRepository.save(adminUser);

        } else {
            log.info("User admin is already initialized.");
        }

        seedRoles();
    }

    private boolean isAdminMissing() {
        return userRepository
                .findByUsername(ADMIN_USERNAME)
                .isEmpty();
    }

    private void seedRoles() {
        if (roleRepository.findByName("ROLE_MANAGER").isEmpty()) {
            roleRepository.save(new Role("ROLE_MANAGER"));
        }
        if (roleRepository.findByName("ROLE_MODERATOR").isEmpty()) {
            roleRepository.save(new Role("ROLE_MODERATOR"));
        }
        if (roleRepository.findByName("ROLE_SUPPORT").isEmpty()) {
            roleRepository.save(new Role("ROLE_SUPPORT"));
        }
    }
}
