package com.amit.mybankapp.accounts.domain.customer;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.customer.vo.Login;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;

import java.util.Objects;

public final class Customer {

    private final CustomerId customerId;

    private final Login login;

    private Profile profile;

    public Customer(CustomerId customerId, Login login, Profile profile) {
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        this.login = Objects.requireNonNull(login, "login must not be null");
        this.profile = Objects.requireNonNull(profile, "profile must not be null");
    }

    public CustomerId getCustomerId() {
        return this.customerId;
    }

    public Login getLogin() {
        return this.login;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void changeProfile(Profile newProfile) {
        this.profile = Objects.requireNonNull(newProfile, "newProfile must not be null");
    }

}
