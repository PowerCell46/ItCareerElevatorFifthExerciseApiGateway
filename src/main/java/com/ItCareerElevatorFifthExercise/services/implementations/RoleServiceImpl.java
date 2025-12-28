package com.ItCareerElevatorFifthExercise.services.implementations;

import com.ItCareerElevatorFifthExercise.entities.Role;
import com.ItCareerElevatorFifthExercise.exceptions.auth.NoSuchRoleException;
import com.ItCareerElevatorFifthExercise.repositories.RoleRepository;
import com.ItCareerElevatorFifthExercise.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getByName(String name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new NoSuchRoleException(String.format("No role found with name %s.", name)));
    }
}
