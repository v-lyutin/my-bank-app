package com.amit.mybankapp.transfer.application.client.accounts;

import com.amit.mybankapp.transfer.application.client.accounts.dto.AccountsTransferResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountsClient {

    AccountsTransferResponse transfer(UUID recipientCustomerId, BigDecimal amount);

}
