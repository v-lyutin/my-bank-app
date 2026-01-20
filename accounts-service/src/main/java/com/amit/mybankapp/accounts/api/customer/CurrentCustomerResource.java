package com.amit.mybankapp.accounts.api.customer;

import com.amit.mybankapp.accounts.api.customer.dto.request.UpdateProfileRequest;
import com.amit.mybankapp.accounts.api.customer.dto.response.CustomerLookupResponse;
import com.amit.mybankapp.accounts.api.customer.dto.response.CustomerResponse;
import com.amit.mybankapp.accounts.api.customer.mapper.CustomerMapper;
import com.amit.mybankapp.accounts.application.customer.CustomerUseCase;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customers/me")
public class CurrentCustomerResource {

    private final CustomerUseCase customerUseCase;

    private final CustomerMapper customerMapper;

    @Autowired
    public CurrentCustomerResource(CustomerUseCase customerUseCase, CustomerMapper customerMapper) {
        this.customerUseCase = customerUseCase;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public ResponseEntity<CustomerResponse> getCurrentCustomer() {
        Customer customer = this.customerUseCase.getCurrentCustomer();
        return ResponseEntity.ok(this.customerMapper.toCustomerResponse(customer));
    }

    @PutMapping(path = "/profile")
    public ResponseEntity<CustomerResponse> updateCurrentProfile(@Valid @RequestBody UpdateProfileRequest request) {
        Profile profile = this.customerMapper.toProfile(request);
        Customer updated = this.customerUseCase.updateCurrentProfile(profile);
        return ResponseEntity.ok(this.customerMapper.toCustomerResponse(updated));
    }

    @GetMapping(path = "/recipient-candidates")
    public ResponseEntity<List<CustomerLookupResponse>> getRecipientCandidatesForCurrentCustomer() {
        List<CustomerLookupResponse> response = this.customerUseCase.getRecipientCandidatesForCurrentCustomer()
                .stream()
                .map(this.customerMapper::toCustomerLookupResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

}
