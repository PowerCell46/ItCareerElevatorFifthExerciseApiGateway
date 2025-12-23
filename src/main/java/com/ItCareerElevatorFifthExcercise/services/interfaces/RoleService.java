package com.ItCareerElevatorFifthExcercise.services.interfaces;

import com.ItCareerElevatorFifthExcercise.entities.Role;

import java.util.Collection;

public interface RoleService {

    Collection<Role> getAll();

    Role save(Role role);
}
