package com.amit.mybankapp.accounts.api.mapper;

import com.amit.mybankapp.accounts.api.dto.request.UpdateProfileRequest;
import com.amit.mybankapp.accounts.api.dto.response.CustomerLookupResponse;
import com.amit.mybankapp.accounts.api.dto.response.CustomerResponse;
import com.amit.mybankapp.accounts.application.model.CustomerLookup;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
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

    public Profile toProfile(UpdateProfileRequest request) {
        return new Profile(
                request.firstName(),
                request.lastName(),
                request.birthDate()
        );
    }

}
