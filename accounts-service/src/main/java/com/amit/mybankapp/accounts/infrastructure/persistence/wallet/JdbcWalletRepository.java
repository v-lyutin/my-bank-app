package com.amit.mybankapp.accounts.infrastructure.persistence.wallet;

import com.amit.mybankapp.accounts.application.repository.WalletRepository;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import com.amit.mybankapp.accounts.infrastructure.persistence.wallet.mapper.WalletRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcWalletRepository implements WalletRepository {

    private static final WalletRowMapper WALLET_ROW_MAPPER = new WalletRowMapper();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcWalletRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Wallet> findByWalletIdAndCustomerId(WalletId walletId, CustomerId customerId) {
        List<Wallet> wallets = this.namedParameterJdbcTemplate.query(
                SqlWalletQuery.FIND_BY_WALLET_ID_AND_CUSTOMER_ID,
                Map.of(
                        "walletId", walletId.value(),
                        "customerId", customerId.value()
                ),
                WALLET_ROW_MAPPER
        );
        return wallets.stream().findFirst();
    }

    @Override
    public Optional<Wallet> findByWalletIdAndCustomerIdForUpdate(WalletId walletId, CustomerId customerId) {
        List<Wallet> wallets = this.namedParameterJdbcTemplate.query(
                SqlWalletQuery.FIND_BY_WALLET_ID_AND_CUSTOMER_ID_FOR_UPDATE,
                Map.of(
                        "walletId", walletId.value(),
                        "customerId", customerId.value()
                ),
                WALLET_ROW_MAPPER
        );
        return wallets.stream().findFirst();
    }

    @Override
    public List<Wallet> findByCustomerId(CustomerId customerId) {
        return this.namedParameterJdbcTemplate.query(
                SqlWalletQuery.FIND_BY_CUSTOMER_ID,
                Map.of("customerId", customerId.value()),
                WALLET_ROW_MAPPER
        );
    }

    @Override
    public void updateBalance(Wallet wallet) {
        this.namedParameterJdbcTemplate.update(
                SqlWalletQuery.UPDATE_BALANCE_BY_WALLET_ID,
                Map.of(
                        "walletId", wallet.getWalletId().value(),
                        "balance", wallet.getBalance().amount()
                )
        );
    }

}
