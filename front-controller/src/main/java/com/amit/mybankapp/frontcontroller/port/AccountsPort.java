package com.amit.mybankapp.frontcontroller.port;

import com.amit.mybankapp.commons.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.dto.accounts.UpdateProfileRequest;

import java.util.List;

public interface AccountsPort {

    CustomerResponse getCurrentCustomer();

    void updateProfile(UpdateProfileRequest updateProfileRequest);

    List<CustomerLookupResponse> getTransferRecipients();

}
