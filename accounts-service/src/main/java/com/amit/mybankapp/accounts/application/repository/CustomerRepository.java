package com.amit.mybankapp.accounts.application.repository;

import com.amit.mybankapp.accounts.application.model.CustomerLookup;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> findByCustomerId(CustomerId customerId);

    void updateProfile(Customer customer);

    List<CustomerLookup> findRecipientCandidatesExcluding(CustomerId excludeCustomerId);

}
