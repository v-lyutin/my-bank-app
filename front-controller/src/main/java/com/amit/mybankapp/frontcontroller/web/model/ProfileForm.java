package com.amit.mybankapp.frontcontroller.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileForm(
        @NotBlank(message = "firstName must not be blank")
        String firstName,

        @NotBlank(message = "lastName must not be blank")
        String lastName,

        @NotNull(message = "birthDate must not be null")
        String birthDate) {

    public static ProfileForm fromCustomer(String firstName, String lastName, String birthDate) {
        return new ProfileForm(firstName, lastName, birthDate);
    }

}
