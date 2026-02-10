package com.amit.mybankapp.frontcontroller.application.result;

import java.util.UUID;

public record CreateTransferResult(
        UUID transferId,
        String status) {
}
