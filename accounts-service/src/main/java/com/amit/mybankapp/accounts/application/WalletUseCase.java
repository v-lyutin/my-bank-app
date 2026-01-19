package com.amit.mybankapp.accounts.application;

import com.amit.mybankapp.accounts.application.exception.ResourceNotFoundException;
import com.amit.mybankapp.accounts.application.repository.WalletRepository;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import com.amit.mybankapp.accounts.infrastructure.provider.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletUseCase {

    private final WalletRepository walletRepository;

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public WalletUseCase(WalletRepository walletRepository, CurrentUserProvider currentUserProvider) {
        this.walletRepository = walletRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional(readOnly = true)
    public Wallet getWalletForCurrentUser(WalletId walletId) {
        CustomerId customerId = this.currentUserProvider.currentUserId();
        return this.walletRepository.findByWalletIdAndCustomerId(walletId, customerId)
                .orElseThrow(() -> ResourceNotFoundException.forWallet(walletId));
    }

    @Transactional(readOnly = true)
    public List<Wallet> getWalletsForCurrentUser() {
        CustomerId customerId = this.currentUserProvider.currentUserId();
        return this.getWalletsForUser(customerId);
    }

    @Transactional(readOnly = true)
    public List<Wallet> getWalletsForUser(CustomerId customerId) {
        return this.walletRepository.findByCustomerId(customerId);
    }

    @Transactional
    public Wallet deposit(WalletId walletId, Money amount) {
        Wallet wallet = this.getWalletForUpdate(walletId);
        wallet.deposit(amount);
        this.walletRepository.updateBalance(wallet);
        return wallet;
    }

    @Transactional
    public Wallet withdraw(WalletId walletId, Money amount) {
        Wallet wallet = this.getWalletForUpdate(walletId);
        wallet.withdraw(amount);
        this.walletRepository.updateBalance(wallet);
        return wallet;
    }

    @Transactional(readOnly = true)
    protected Wallet getWalletForUpdate(WalletId walletId) {
        CustomerId customerId = this.currentUserProvider.currentUserId();
        return this.walletRepository.findByWalletIdAndCustomerIdForUpdate(walletId, customerId)
                .orElseThrow(() -> ResourceNotFoundException.forWallet(walletId));
    }

}
