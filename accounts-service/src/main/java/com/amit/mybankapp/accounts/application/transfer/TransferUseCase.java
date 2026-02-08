package com.amit.mybankapp.accounts.application.transfer;

import com.amit.mybankapp.accounts.application.transfer.exception.InvalidTransferException;
import com.amit.mybankapp.accounts.application.transfer.model.TransferResult;
import com.amit.mybankapp.accounts.application.wallet.repository.WalletRepository;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.apierrors.server.exception.base.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransferUseCase {

    private static final Comparator<CustomerId> BY_CUSTOMER_ID = Comparator.comparing(CustomerId::value);

    private final WalletRepository walletRepository;

    @Autowired
    public TransferUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public TransferResult transfer(CustomerId senderCustomerId, CustomerId recipientCustomerId, Money amount) {
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
        List<CustomerId> orderedCustomerIds = Stream.of(senderId, recipientId).sorted(BY_CUSTOMER_ID).toList();

        CustomerId firstCustomerId = orderedCustomerIds.get(0);
        CustomerId secondCustomerId = orderedCustomerIds.get(1);

        Wallet firstLockedWallet = lockPrimaryWallet(firstCustomerId);
        Wallet secondLockedWallet = lockPrimaryWallet(secondCustomerId);

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
