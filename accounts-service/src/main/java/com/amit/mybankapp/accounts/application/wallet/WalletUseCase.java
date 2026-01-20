package com.amit.mybankapp.accounts.application.wallet;

import com.amit.mybankapp.accounts.application.common.exception.ResourceNotFoundException;
import com.amit.mybankapp.accounts.application.wallet.repository.WalletRepository;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.infrastructure.provider.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletUseCase {

    private final WalletRepository walletRepository;

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public WalletUseCase(WalletRepository walletRepository, CurrentUserProvider currentUserProvider) {
        this.walletRepository = walletRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public Wallet deposit(Money amount) {
        Wallet wallet = this.getPrimaryWalletForUpdate();
        wallet.deposit(amount);
        this.walletRepository.updateBalance(wallet);
        return wallet;
    }

    @Transactional
    public Wallet withdraw(Money amount) {
        Wallet wallet = this.getPrimaryWalletForUpdate();
        wallet.withdraw(amount);
        this.walletRepository.updateBalance(wallet);
        return wallet;
    }

    @Transactional
    protected Wallet getPrimaryWalletForUpdate() {
        CustomerId customerId = this.currentUserProvider.currentUserId();
        return this.walletRepository.findPrimaryByCustomerIdForUpdate(customerId)
                .orElseThrow(() -> ResourceNotFoundException.forWalletOfCustomer(customerId));
    }

}
