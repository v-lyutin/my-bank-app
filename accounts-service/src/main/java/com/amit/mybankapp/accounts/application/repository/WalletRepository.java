package com.amit.mybankapp.accounts.application.repository;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;

import java.util.Optional;

public interface WalletRepository {

    Optional<Wallet> findPrimaryByCustomerId(CustomerId customerId);

    Optional<Wallet> findPrimaryByCustomerIdForUpdate(CustomerId customerId);

    void updateBalance(Wallet wallet);

}
