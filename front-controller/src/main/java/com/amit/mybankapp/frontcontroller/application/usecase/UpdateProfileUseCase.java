package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import com.amit.mybankapp.frontcontroller.application.command.UpdateProfileCommand;
import com.amit.mybankapp.frontcontroller.application.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileUseCase {

    private final AccountsClient accountsClient;

    @Autowired
    public UpdateProfileUseCase(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    public void updateProfile(UpdateProfileCommand command) {
        if (command == null) {
            throw new ValidationException("Profile data must be provided");
        }

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(
                command.firstName(),
                command.lastName(),
                command.birthDate()
        );

        this.accountsClient.updateProfileForCurrentCustomer(updateProfileRequest);
    }

}
