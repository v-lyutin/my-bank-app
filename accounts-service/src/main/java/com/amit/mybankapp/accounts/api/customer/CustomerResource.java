package com.amit.mybankapp.accounts.api.customer;

import com.amit.mybankapp.accounts.api.customer.mapper.CustomerMapper;
import com.amit.mybankapp.accounts.application.customer.CustomerUseCase;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class CustomerResource {

    private final CustomerUseCase customerUseCase;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerResource(CustomerUseCase customerUseCase, CustomerMapper customerMapper) {
        this.customerUseCase = customerUseCase;
        this.customerMapper = customerMapper;
    }

    @GetMapping(path = "/internal/customers/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerByCustomerId(@PathVariable(name = "customerId") UUID customerId) {
        Customer customer = this.customerUseCase.getCustomerByCustomerId(CustomerId.of(customerId));
        return ResponseEntity.ok(this.customerMapper.toCustomerResponse(customer));
    }

    @GetMapping(path = "/customers/me")
    public ResponseEntity<CustomerResponse> getCurrentCustomer() {
        Customer customer = this.customerUseCase.getCurrentCustomer();
        return ResponseEntity.ok(this.customerMapper.toCustomerResponse(customer));
    }

    @PutMapping(path = "/customers/me/profile")
    public ResponseEntity<CustomerResponse> updateProfileForCurrentCustomer(@Valid @RequestBody UpdateProfileRequest request) {
        Profile profile = this.customerMapper.toProfile(request);
        Customer updatedCustomer = this.customerUseCase.updateProfileForCurrentCustomer(profile);
        return ResponseEntity.ok(this.customerMapper.toCustomerResponse(updatedCustomer));
    }

    @GetMapping(path = "/customers/me/recipient-candidates")
    public ResponseEntity<List<CustomerLookupResponse>> getRecipientCandidatesForCurrentCustomer() {
        List<CustomerLookupResponse> response = this.customerUseCase.getRecipientCandidatesForCurrentCustomer()
                .stream()
                .map(this.customerMapper::toCustomerLookupResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

}
