package com.amit.mybankapp.accounts.application;

import com.amit.mybankapp.accounts.application.exception.ResourceNotFoundException;
import com.amit.mybankapp.accounts.application.model.CustomerLookup;
import com.amit.mybankapp.accounts.application.repository.CustomerRepository;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import com.amit.mybankapp.accounts.infrastructure.provider.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerUseCase {

    private final CustomerRepository customerRepository;

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public CustomerUseCase(CustomerRepository customerRepository, CurrentUserProvider currentUserProvider) {
        this.customerRepository = customerRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional(readOnly = true)
    public Customer getCurrentCustomer() {
        CustomerId customerId = this.currentUserProvider.currentUserId();
        return this.customerRepository.findByCustomerId(customerId).orElseThrow(() -> ResourceNotFoundException.forAccount(customerId));
    }

    @Transactional
    public Customer updateCurrentProfile(Profile profile) {
        Customer currentCustomer = this.getCurrentCustomer();
        currentCustomer.changeProfile(profile);
        this.customerRepository.updateProfile(currentCustomer);
        return currentCustomer;
    }

    @Transactional(readOnly = true)
    public List<CustomerLookup> getRecipientCandidatesForCurrentCustomer() {
        CustomerId currentCustomerId = this.currentUserProvider.currentUserId();
        return this.customerRepository.findRecipientCandidatesExcluding(currentCustomerId);
    }

}
