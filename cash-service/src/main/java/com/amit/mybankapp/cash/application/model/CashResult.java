package com.amit.mybankapp.cash.application.model;

import com.amit.mybankapp.cash.application.model.type.CashStatus;

import java.util.UUID;

public record CashResult(UUID operationId, CashStatus status) {}
