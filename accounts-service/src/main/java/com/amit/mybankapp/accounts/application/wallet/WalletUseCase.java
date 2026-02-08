package com.amit.mybankapp.accounts.application.wallet;

import com.amit.mybankapp.accounts.application.wallet.model.WalletOperationResult;
import com.amit.mybankapp.accounts.application.wallet.repository.WalletRepository;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.infrastructure.provider.CurrentUserProvider;
import com.amit.mybankapp.apierrors.server.exception.base.ResourceNotFoundException;
import com.amit.mybankapp.commons.model.type.WalletOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletUseCase.class);

    private final WalletRepository walletRepository;

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public WalletUseCase(WalletRepository walletRepository, CurrentUserProvider currentUserProvider) {
        this.walletRepository = walletRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional(readOnly = true)
    public Wallet getPrimaryWalletForCurrentUser() {
        CustomerId currentCustomerId = this.currentUserProvider.currentUserId();
        return this.walletRepository.findPrimaryByCustomerId(currentCustomerId)
                .orElseThrow(() -> ResourceNotFoundException.forWalletOfCustomer(currentCustomerId.value()));
    }

    @Transactional
    public WalletOperationResult deposit(CustomerId customerId, Money amount) {
        Wallet wallet = this.getPrimaryWalletForUpdate(customerId);
        wallet.deposit(amount);
        this.walletRepository.updateBalance(wallet);

        LOGGER.info(
                "Deposit completed: customerId={}, walletId={}, amount={}",
                wallet.getCustomerId().value(),
                wallet.getWalletId().value(),
                amount.amount()
        );

        return new WalletOperationResult(
                null,
                WalletOperationType.DEPOSIT.name(),
                wallet.getWalletId().value(),
                wallet.getCustomerId().value(),
                amount.amount()
        );
    }

    @Transactional
    public WalletOperationResult withdraw(CustomerId customerId, Money amount) {
        Wallet wallet = this.getPrimaryWalletForUpdate(customerId);
        wallet.withdraw(amount);
        this.walletRepository.updateBalance(wallet);

        LOGGER.info(
                "Withdraw completed: customerId={}, walletId={}, amount={}",
                wallet.getCustomerId().value(),
                wallet.getWalletId().value(),
                amount.amount()
        );

        return new WalletOperationResult(
                null,
                WalletOperationType.WITHDRAW.name(),
                wallet.getWalletId().value(),
                wallet.getCustomerId().value(),
                amount.amount()
        );
    }

    @Transactional
    protected Wallet getPrimaryWalletForUpdate(CustomerId customerId) {
        return this.walletRepository.findPrimaryByCustomerIdForUpdate(customerId)
                .orElseThrow(() -> ResourceNotFoundException.forWalletOfCustomer(customerId.value()));
    }

}
