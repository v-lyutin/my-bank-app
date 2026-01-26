package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;
import com.amit.mybankapp.frontcontroller.application.result.HomePageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GetHomePageUseCase {

    private final AccountsClient accountsClient;

    @Autowired
    public GetHomePageUseCase(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    public HomePageResult getHomePage() {
        CustomerResponse currentCustomer = this.accountsClient.getCurrentCustomer();
        WalletResponse wallet = this.accountsClient.getPrimaryWalletForCurrentUser();
        List<CustomerLookupResponse> recipients = this.accountsClient.getTransferRecipients();

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
