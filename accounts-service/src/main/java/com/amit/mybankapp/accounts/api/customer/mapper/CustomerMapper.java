package com.amit.mybankapp.accounts.api.customer.mapper;

import com.amit.mybankapp.accounts.application.customer.model.CustomerLookup;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import org.springframework.stereotype.Component;

@Component
public final class CustomerMapper {

    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getCustomerId().value(),
                customer.getLogin().value(),
                customer.getProfile().firstName(),
                customer.getProfile().lastName(),
                customer.getProfile().birthDate()
        );
    }

    public CustomerLookupResponse toCustomerLookupResponse(CustomerLookup customerLookup) {
        return new CustomerLookupResponse(
                customerLookup.userId(),
                customerLookup.login(),
                customerLookup.firstName(),
                customerLookup.lastName()
        );
    }

    public Profile toProfile(UpdateProfileRequest updateProfileRequest) {
        return new Profile(
                updateProfileRequest.firstName(),
                updateProfileRequest.lastName(),
                updateProfileRequest.birthDate()
        );
    }

}
