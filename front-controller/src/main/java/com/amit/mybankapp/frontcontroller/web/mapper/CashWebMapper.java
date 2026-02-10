package com.amit.mybankapp.frontcontroller.web.mapper;

import com.amit.mybankapp.frontcontroller.application.command.WalletOperationCommand;
import com.amit.mybankapp.frontcontroller.web.model.CashForm;
import org.springframework.stereotype.Component;

@Component
public final class CashWebMapper {

    public WalletOperationCommand toWalletOperationCommand(CashForm cashForm) {
        return new WalletOperationCommand(cashForm.amount());
    }

}
