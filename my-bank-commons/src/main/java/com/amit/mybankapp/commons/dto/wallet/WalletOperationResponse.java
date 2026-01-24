package com.amit.mybankapp.commons.dto.wallet;

import java.util.UUID;

public record WalletOperationResponse(
        UUID operationId,
        String status) {
}
