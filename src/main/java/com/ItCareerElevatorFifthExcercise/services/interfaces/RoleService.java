package com.ItCareerElevatorFifthExcercise.services.interfaces;

import com.ItCareerElevatorFifthExcercise.entities.Role;

import java.util.Collection;
import java.util.Optional;

public interface RoleService {

    Optional<Role> findByName(String name);

    Collection<Role> getAll();

    Role save(Role role);
}
