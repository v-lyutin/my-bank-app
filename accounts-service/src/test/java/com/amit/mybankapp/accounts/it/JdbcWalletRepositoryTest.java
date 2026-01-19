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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(value = JdbcWalletRepository.class)
class JdbcWalletRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private JdbcWalletRepository jdbcWalletRepository;

    @Test
    @DisplayName(value = "Should return wallet when wallet exists by walletId and customerId")
    void findByWalletIdAndCustomerId_shouldReturnWalletWhenWalletExistsByWalletIdAndCustomerId() {
        UUID customerId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();

        this.insertCustomerRow(customerId, "john.doe", "John", "Doe");
        this.insertWalletRow(walletId, customerId, new BigDecimal("10.00"));

        Optional<Wallet> result = this.jdbcWalletRepository.findByWalletIdAndCustomerId(
                new WalletId(walletId),
                new CustomerId(customerId)
        );

        assertTrue(result.isPresent());

        Wallet wallet = result.get();

        assertAll(
                () -> assertEquals(new WalletId(walletId), wallet.getWalletId()),
                () -> assertEquals(new CustomerId(customerId), wallet.getCustomerId()),
                () -> assertEquals(new BigDecimal("10.00"), wallet.getBalance().amount())
        );
    }

    @Test
    @DisplayName(value = "Should return empty optional when wallet does not exist by walletId and customerId")
    void findByWalletIdAndCustomerId_shouldReturnEmptyOptionalWhenWalletDoesNotExistByWalletIdAndCustomerId() {
        Optional<Wallet> result = this.jdbcWalletRepository.findByWalletIdAndCustomerId(
                new WalletId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID())
        );

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName(value = "Should return wallets when wallets exist for customerId")
    void findByCustomerId_shouldReturnWalletsWhenWalletsExistForCustomerId() {
        UUID customerId = UUID.randomUUID();
        UUID firstWalletId = UUID.randomUUID();
        UUID secondWalletId = UUID.randomUUID();

        this.insertCustomerRow(customerId, "jane.doe", "Jane", "Doe");

        this.insertWalletRow(firstWalletId, customerId, new BigDecimal("1.00"));
        this.insertWalletRow(secondWalletId, customerId, new BigDecimal("2.50"));

        List<Wallet> wallets = this.jdbcWalletRepository.findByCustomerId(new CustomerId(customerId));

        assertEquals(2, wallets.size());

        assertTrue(wallets.stream().anyMatch(wallet ->
                wallet.getWalletId().equals(new WalletId(firstWalletId))
                        && wallet.getCustomerId().equals(new CustomerId(customerId))
                        && wallet.getBalance().amount().compareTo(new BigDecimal("1.00")) == 0
        ));

        assertTrue(wallets.stream().anyMatch(wallet ->
                wallet.getWalletId().equals(new WalletId(secondWalletId))
                        && wallet.getCustomerId().equals(new CustomerId(customerId))
                        && wallet.getBalance().amount().compareTo(new BigDecimal("2.50")) == 0
        ));
    }

    @Test
    @DisplayName(value = "Should update wallet balance when updateBalance is called")
    void updateBalance_shouldUpdateWalletBalanceWhenUpdateBalanceIsCalled() {
        UUID customerIdUuid = UUID.randomUUID();
        UUID walletIdUuid = UUID.randomUUID();

        this.insertCustomerRow(customerIdUuid, "balance.user", "Balance", "User");
        this.insertWalletRow(walletIdUuid, customerIdUuid, new BigDecimal("10.00"));

        WalletId walletId = new WalletId(walletIdUuid);
        CustomerId customerId = new CustomerId(customerIdUuid);

        Wallet wallet = this.jdbcWalletRepository.findByWalletIdAndCustomerId(walletId, customerId).orElseThrow();

        wallet.deposit(new Money(new BigDecimal("2.55")));

        this.jdbcWalletRepository.updateBalance(wallet);

        Wallet reloadedWallet = this.jdbcWalletRepository.findByWalletIdAndCustomerId(walletId, customerId).orElseThrow();

        assertEquals(new BigDecimal("12.55"), reloadedWallet.getBalance().amount());
    }

    @Test
    @DisplayName(value = "Should return wallet when wallet exists by walletId and customerId for update")
    void findByWalletIdAndCustomerIdForUpdate_shouldReturnWalletWhenWalletExistsByWalletIdAndCustomerIdForUpdate() {
        UUID customerId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();

        this.insertCustomerRow(customerId, "locking.user", "Locking", "User");
        this.insertWalletRow(walletId, customerId, new BigDecimal("5.00"));

        Optional<Wallet> result = this.jdbcWalletRepository.findByWalletIdAndCustomerIdForUpdate(
                new WalletId(walletId),
                new CustomerId(customerId)
        );

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("5.00"), result.get().getBalance().amount());
    }

    private void insertCustomerRow(UUID customerId, String login, String firstName, String lastName) {
        this.namedParameterJdbcTemplate.update(
                """
                INSERT INTO accounts.customers (customer_id, login, first_name, last_name, birth_date)
                VALUES (:customerId, :login, :firstName, :lastName, :birthDate)
                """,
                Map.of(
                        "customerId", customerId,
                        "login", login,
                        "firstName", firstName,
                        "lastName", lastName,
                        "birthDate", LocalDate.now().minusYears(20)
                )
        );
    }

    private void insertWalletRow(UUID walletId, UUID customerId, BigDecimal balance) {
        this.namedParameterJdbcTemplate.update(
                """
                INSERT INTO accounts.wallets (wallet_id, customer_id, balance)
                VALUES (:walletId, :customerId, :balance)
                """,
                Map.of(
                        "walletId", walletId,
                        "customerId", customerId,
                        "balance", balance
                )
        );
    }

}
