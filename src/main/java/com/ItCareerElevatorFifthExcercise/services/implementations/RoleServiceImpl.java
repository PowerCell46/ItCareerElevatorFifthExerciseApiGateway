package com.ItCareerElevatorFifthExcercise.services.implementations;

import com.ItCareerElevatorFifthExcercise.entities.Role;
import com.ItCareerElevatorFifthExcercise.repositories.RoleRepository;
import com.ItCareerElevatorFifthExcercise.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Collection<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role save(Role role) {
        log.info("Persisting role with name {} to the database.", role.getName());

        return roleRepository.save(role);
    }
}
