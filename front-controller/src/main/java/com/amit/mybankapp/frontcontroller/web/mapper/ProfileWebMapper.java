package com.amit.mybankapp.frontcontroller.web.mapper;

import com.amit.mybankapp.frontcontroller.application.command.UpdateProfileCommand;
import com.amit.mybankapp.frontcontroller.web.model.ProfileForm;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public final class ProfileWebMapper {

    public UpdateProfileCommand toUpdateProfileCommand(ProfileForm form) {
        return new UpdateProfileCommand(
                form.firstName(),
                form.lastName(),
                parseBirthDate(form.birthDate())
        );
    }

    private LocalDate parseBirthDate(String birthDate) {
        if (birthDate == null || birthDate.isBlank()) {
            return null;
        }
        return LocalDate.parse(birthDate);
    }

}
