package com.amit.mybankapp.frontcontroller.application.command;

import java.time.LocalDate;

public record UpdateProfileCommand(
        String firstName,
        String lastName,
        LocalDate birthDate) {
}
