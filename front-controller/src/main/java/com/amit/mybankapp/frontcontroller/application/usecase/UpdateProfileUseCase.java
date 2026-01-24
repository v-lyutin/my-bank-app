package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.frontcontroller.application.command.UpdateProfileCommand;

public interface UpdateProfileUseCase {

    void updateProfile(UpdateProfileCommand command);

}
