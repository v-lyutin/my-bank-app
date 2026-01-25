package com.amit.mybankapp.frontcontroller.application.service;

import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import com.amit.mybankapp.frontcontroller.application.command.UpdateProfileCommand;
import com.amit.mybankapp.frontcontroller.application.exception.ValidationException;
import com.amit.mybankapp.frontcontroller.application.usecase.UpdateProfileUseCase;
import com.amit.mybankapp.frontcontroller.port.AccountsPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileApplicationService implements UpdateProfileUseCase {

    private final AccountsPort accountsPort;

    @Autowired
    public UpdateProfileApplicationService(AccountsPort accountsPort) {
        this.accountsPort = accountsPort;
    }

    @Override
    public void updateProfile(UpdateProfileCommand command) {
        if (command == null) {
            throw new ValidationException("Profile data must be provided");
        }

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(
                command.firstName(),
                command.lastName(),
                command.birthDate()
        );

        this.accountsPort.updateProfile(updateProfileRequest);
    }

}
