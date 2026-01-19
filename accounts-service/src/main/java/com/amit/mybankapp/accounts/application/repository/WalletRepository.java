package com.amit.mybankapp.accounts.application.repository;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;

import java.util.List;
import java.util.Optional;

public interface WalletRepository {

    Optional<Wallet> findByWalletIdAndCustomerId(WalletId walletId, CustomerId customerId);

    Optional<Wallet> findByWalletIdAndCustomerIdForUpdate(WalletId walletId, CustomerId customerId);

    List<Wallet> findByCustomerId(CustomerId customerId);

    void updateBalance(Wallet wallet);

}
