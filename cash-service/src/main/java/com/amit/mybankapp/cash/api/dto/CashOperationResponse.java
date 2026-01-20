package com.amit.mybankapp.cash.api.dto;

import com.amit.mybankapp.cash.application.model.type.CashStatus;

import java.util.UUID;

public record CashOperationResponse(
        UUID operationId,
        CashStatus status) {
}
