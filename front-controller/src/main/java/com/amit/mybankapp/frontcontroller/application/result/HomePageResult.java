package com.amit.mybankapp.frontcontroller.application.result;

import java.math.BigDecimal;
import java.util.List;

public record HomePageResult(
        CurrentCustomerView currentCustomerView,
        BigDecimal balance,
        List<TransferRecipientView> recipients) {

    public record CurrentCustomerView(
            String login,
            String firstName,
            String lastName,
            String birthDate) {
    }

    public record TransferRecipientView(
            String recipientId,
            String login,
            String firstName,
            String lastName) {
    }

}
