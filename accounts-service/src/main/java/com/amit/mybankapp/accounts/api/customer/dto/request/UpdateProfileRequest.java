package com.amit.mybankapp.accounts.api.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateProfileRequest(
        @NotBlank(message = "firstName must not be blank")
        String firstName,

        @NotBlank(message = "lastName must not be blank")
        String lastName,

        @NotNull(message = "birthDate must not be null")
        LocalDate birthDate) {
}
