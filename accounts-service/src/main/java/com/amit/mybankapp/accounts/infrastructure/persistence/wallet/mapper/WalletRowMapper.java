package com.amit.mybankapp.accounts.infrastructure.persistence.wallet.mapper;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class WalletRowMapper implements RowMapper<Wallet> {

    @Override
    public Wallet mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        WalletId walletId = new WalletId((UUID) resultSet.getObject("wallet_id"));
        CustomerId customerId = new CustomerId((UUID) resultSet.getObject("customer_id"));
        Money balance = Money.of(resultSet.getBigDecimal("balance"));

        return new Wallet(walletId, customerId, balance);
    }

}
