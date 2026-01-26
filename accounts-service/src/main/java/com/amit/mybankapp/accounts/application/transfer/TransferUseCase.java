package com.amit.mybankapp.accounts.application.transfer;

import com.amit.mybankapp.accounts.application.transfer.exception.InvalidTransferException;
import com.amit.mybankapp.accounts.application.transfer.model.TransferResult;
import com.amit.mybankapp.accounts.application.wallet.repository.WalletRepository;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.infrastructure.provider.CurrentUserProvider;
import com.amit.mybankapp.apierrors.server.exception.base.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferUseCase {

    private final WalletRepository walletRepository;

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public TransferUseCase(WalletRepository walletRepository, CurrentUserProvider currentUserProvider) {
        this.walletRepository = walletRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public TransferResult transfer(UUID recipientCustomerIdRaw, Money amount) {
        CustomerId senderCustomerId = this.currentUserProvider.currentUserId();
        CustomerId recipientCustomerId = new CustomerId(recipientCustomerIdRaw);

        if (senderCustomerId.equals(recipientCustomerId)) {
            throw new InvalidTransferException("cannot transfer to self");
        }

        LockedWalletPair pair = this.lockBothPrimaryWallets(senderCustomerId, recipientCustomerId);

        Wallet senderWallet = pair.senderWallet();
        Wallet recipientWallet = pair.recipientWallet();

        senderWallet.withdraw(amount);
        recipientWallet.deposit(amount);

        this.walletRepository.updateBalance(senderWallet);
        this.walletRepository.updateBalance(recipientWallet);

        return new TransferResult(
                senderCustomerId.value(),
                recipientCustomerId.value(),
                amount.amount()
        );
    }

    private LockedWalletPair lockBothPrimaryWallets(CustomerId senderId, CustomerId recipientId) {

        CustomerId firstCustomerId;
        CustomerId secondCustomerId;

        if (senderId.value().compareTo(recipientId.value()) <= 0) {
            firstCustomerId = senderId;
            secondCustomerId = recipientId;
        } else {
            firstCustomerId = recipientId;
            secondCustomerId = senderId;
        }

        Wallet firstLockedWallet = this.lockPrimaryWallet(firstCustomerId);
        Wallet secondLockedWallet = this.lockPrimaryWallet(secondCustomerId);

        Wallet senderLockedWallet = senderId.equals(firstCustomerId) ? firstLockedWallet : secondLockedWallet;
        Wallet recipientLockedWallet = senderId.equals(firstCustomerId) ? secondLockedWallet : firstLockedWallet;

        return new LockedWalletPair(senderLockedWallet, recipientLockedWallet);
    }


    private Wallet lockPrimaryWallet(CustomerId customerId) {
        return this.walletRepository.findPrimaryByCustomerIdForUpdate(customerId)
                .orElseThrow(() -> ResourceNotFoundException.forWalletOfCustomer(customerId.value()));
    }

    private record LockedWalletPair(Wallet senderWallet, Wallet recipientWallet) {}

}
