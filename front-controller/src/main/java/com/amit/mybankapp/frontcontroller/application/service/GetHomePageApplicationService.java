package com.amit.mybankapp.frontcontroller.application.service;

import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;
import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import com.amit.mybankapp.frontcontroller.application.usecase.GetHomePageUseCase;
import com.amit.mybankapp.frontcontroller.port.AccountsPort;
import com.amit.mybankapp.frontcontroller.port.WalletPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GetHomePageApplicationService implements GetHomePageUseCase {

    private final AccountsPort accountsPort;

    private final WalletPort walletPort;

    @Autowired
    public GetHomePageApplicationService(AccountsPort accountsPort, WalletPort walletPort) {
        this.accountsPort = accountsPort;
        this.walletPort = walletPort;
    }

    @Override
    public HomePageResult getHomePage() {
        CustomerResponse currentCustomer = this.accountsPort.getCurrentCustomer();
        WalletResponse wallet = this.walletPort.getWallet();
        List<CustomerLookupResponse> recipients = this.accountsPort.getTransferRecipients();

        return new HomePageResult(
                toCurrentCustomerView(currentCustomer),
                toBalance(wallet),
                toTransferRecipientViews(recipients)
        );
    }

    private static HomePageResult.CurrentCustomerView toCurrentCustomerView(CustomerResponse customerResponse) {
        return new HomePageResult.CurrentCustomerView(
                customerResponse.login(),
                customerResponse.firstName(),
                customerResponse.lastName(),
                customerResponse.birthDate() != null ? customerResponse.birthDate().toString() : null
        );
    }

    private static BigDecimal toBalance(WalletResponse walletResponse) {
        return walletResponse.balance();
    }

    private static List<HomePageResult.TransferRecipientView> toTransferRecipientViews(List<CustomerLookupResponse> customerLookupResponses) {
        return customerLookupResponses.stream()
                .map(customerLookupResponse -> new HomePageResult.TransferRecipientView(
                        customerLookupResponse.userId().toString(),
                        customerLookupResponse.login(),
                        customerLookupResponse.firstName(),
                        customerLookupResponse.lastName()
                ))
                .toList();
    }

}
