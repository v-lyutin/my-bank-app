package com.amit.mybankapp.accounts.it;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import com.amit.mybankapp.accounts.infrastructure.persistence.wallet.JdbcWalletRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(value = JdbcWalletRepository.class)
class JdbcWalletRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private JdbcWalletRepository jdbcWalletRepository;

    @Test
    @DisplayName(value = "Should return empty when primary wallet does not exist for customer")
    void findPrimaryByCustomerId_shouldReturnEmptyWhenPrimaryWalletDoesNotExistForCustomer() {
        CustomerId customerId = CustomerId.of(UUID.randomUUID());
        this.insertCustomer(customerId);

        Optional<Wallet> wallet = this.jdbcWalletRepository.findPrimaryByCustomerId(customerId);

        assertTrue(wallet.isEmpty());
    }

    @Test
    @DisplayName(value = "Should return primary wallet when it exists for customer")
    void findPrimaryByCustomerId_shouldReturnPrimaryWalletWhenItExistsForCustomer() {
        CustomerId customerId = CustomerId.of(UUID.randomUUID());
        this.insertCustomer(customerId);

        WalletId walletId = WalletId.of(UUID.randomUUID());
        this.insertWallet(walletId, customerId, Money.of(new BigDecimal("10.50")), true);

        Optional<Wallet> wallet = this.jdbcWalletRepository.findPrimaryByCustomerId(customerId);

        assertTrue(wallet.isPresent());
        assertEquals(walletId, wallet.get().getWalletId());
        assertEquals(customerId, wallet.get().getCustomerId());
        assertEquals(new BigDecimal("10.50"), wallet.get().getBalance().amount());
    }

    @Test
    @DisplayName(value = "Should return empty when only non primary wallet exists for customer")
    void findPrimaryByCustomerId_shouldReturnEmptyWhenOnlyNonPrimaryWalletExistsForCustomer() {
        CustomerId customerId = CustomerId.of(UUID.randomUUID());
        this.insertCustomer(customerId);

        WalletId walletId = WalletId.of(UUID.randomUUID());
        this.insertWallet(walletId, customerId, Money.of(new BigDecimal("99.99")), false);

        Optional<Wallet> wallet = this.jdbcWalletRepository.findPrimaryByCustomerId(customerId);

        assertTrue(wallet.isEmpty());
    }

    @Test
    @DisplayName(value = "Should return primary wallet for update when it exists for customer")
    void findPrimaryByCustomerIdForUpdate_shouldReturnPrimaryWalletWhenItExistsForCustomer() {
        CustomerId customerId = CustomerId.of(UUID.randomUUID());
        this.insertCustomer(customerId);

        WalletId walletId = WalletId.of(UUID.randomUUID());
        this.insertWallet(walletId, customerId, Money.of(new BigDecimal("15.00")), true);

        Optional<Wallet> wallet = this.jdbcWalletRepository.findPrimaryByCustomerIdForUpdate(customerId);

        assertTrue(wallet.isPresent());
        assertEquals(walletId, wallet.get().getWalletId());
        assertEquals(customerId, wallet.get().getCustomerId());
        assertEquals(new BigDecimal("15.00"), wallet.get().getBalance().amount());
    }

    @Test
    @DisplayName(value = "Should update balance when wallet exists")
    void updateBalance_shouldUpdateBalanceWhenWalletExists() {
        CustomerId customerId = CustomerId.of(UUID.randomUUID());
        this.insertCustomer(customerId);

        WalletId walletId = WalletId.of(UUID.randomUUID());
        this.insertWallet(walletId, customerId, Money.of(new BigDecimal("10.00")), true);

        Wallet wallet = this.jdbcWalletRepository.findPrimaryByCustomerId(customerId).orElseThrow();
        wallet.deposit(Money.of(new BigDecimal("15.75")));

        this.jdbcWalletRepository.updateBalance(wallet);

        Money storedBalance = this.readBalanceByWalletId(walletId);

        assertEquals(new BigDecimal("25.75"), storedBalance.amount());
    }

    @Test
    @DisplayName(value = "Should not update other wallet when updating balance for specific wallet")
    void updateBalance_shouldNotUpdateOtherWalletWhenUpdatingBalanceForSpecificWallet() {
        CustomerId firstCustomerId = CustomerId.of(UUID.randomUUID());
        CustomerId secondCustomerId = CustomerId.of(UUID.randomUUID());
        this.insertCustomer(firstCustomerId);
        this.insertCustomer(secondCustomerId);

        WalletId firstWalletId = WalletId.of(UUID.randomUUID());
        WalletId secondWalletId = WalletId.of(UUID.randomUUID());

        this.insertWallet(firstWalletId, firstCustomerId, Money.of(new BigDecimal("10.00")), true);
        this.insertWallet(secondWalletId, secondCustomerId, Money.of(new BigDecimal("50.00")), true);

        Wallet firstWallet = this.jdbcWalletRepository.findPrimaryByCustomerId(firstCustomerId).orElseThrow();
        firstWallet.deposit(Money.of(new BigDecimal("5.00")));

        this.jdbcWalletRepository.updateBalance(firstWallet);

        Money firstStoredBalance = this.readBalanceByWalletId(firstWalletId);
        Money secondStoredBalance = this.readBalanceByWalletId(secondWalletId);

        assertEquals(new BigDecimal("15.00"), firstStoredBalance.amount());
        assertEquals(new BigDecimal("50.00"), secondStoredBalance.amount());
    }

    private void insertCustomer(CustomerId customerId) {
        String login = this.createValidUniqueLogin(customerId);

        this.namedParameterJdbcTemplate.update(
                """
                        INSERT INTO accounts.customers(customer_id, login, first_name, last_name, birth_date)
                        VALUES (:customerId, :login, :firstName, :lastName, :birthDate)
                        """,
                Map.of(
                        "customerId", customerId.value(),
                        "login", login,
                        "firstName", "john",
                        "lastName", "doe",
                        "birthDate", LocalDate.of(1990, 1, 15)
                )
        );
    }

    private String createValidUniqueLogin(CustomerId customerId) {
        String suffix = customerId.value().toString().replace("-", "");
        String login = "user" + suffix;
        return login.toLowerCase();
    }

    private void insertWallet(WalletId walletId, CustomerId customerId, Money balance, boolean isPrimary) {
        this.namedParameterJdbcTemplate.update(
                """
                        INSERT INTO accounts.wallets(wallet_id, customer_id, balance, is_primary)
                        VALUES (:walletId, :customerId, :balance, :isPrimary)
                        """,
                Map.of(
                        "walletId", walletId.value(),
                        "customerId", customerId.value(),
                        "balance", balance.amount(),
                        "isPrimary", isPrimary
                )
        );
    }

    private Money readBalanceByWalletId(WalletId walletId) {
        BigDecimal balance = this.namedParameterJdbcTemplate.queryForObject(
                """
                        SELECT balance
                        FROM accounts.wallets
                        WHERE wallet_id = :walletId
                        """,
                Map.of("walletId", walletId.value()),
                BigDecimal.class
        );

        assertNotNull(balance);
        return Money.of(balance);
    }

}
