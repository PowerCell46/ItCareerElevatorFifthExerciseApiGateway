package com.ItCareerElevatorFifthExercise;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("---> Message from CommandLineRunnerImpl...!");
    }
}
