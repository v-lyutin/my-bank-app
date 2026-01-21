package com.amit.mybankapp.transfer.it;

import com.amit.mybankapp.transfer.infrastructure.audit.model.TransferAuditRecord;
import com.amit.mybankapp.transfer.infrastructure.audit.model.type.TransferStatus;
import com.amit.mybankapp.transfer.infrastructure.audit.repository.jdbc.JdbcTransferRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Import(value = JdbcTransferRepository.class)
class JdbcTransferRepositoryTest extends AbstractRepositoryIT {

    @org.springframework.beans.factory.annotation.Autowired
    private JdbcTransferRepository jdbcTransferRepository;

    @Test
    @DisplayName(value = "Should save transfer audit record when record is valid")
    void save_shouldSaveTransferAuditRecordWhenRecordIsValid() {
        UUID transferId = UUID.randomUUID();
        UUID senderCustomerId = UUID.randomUUID();
        UUID recipientCustomerId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10.50");

        TransferAuditRecord record = TransferAuditRecord.accepted(
                transferId,
                senderCustomerId,
                recipientCustomerId,
                amount
        );

        this.jdbcTransferRepository.save(record);

        Map<String, Object> row = this.namedParameterJdbcTemplate.queryForMap(
                """
                SELECT transfer_id, sender_customer_id, recipient_customer_id, amount, status, created_at
                FROM transfers.transfer_operations
                WHERE transfer_id = :transferId
                """,
                Map.of("transferId", transferId)
        );

        assertThat(row.get("transfer_id")).isEqualTo(transferId);
        assertThat(row.get("sender_customer_id")).isEqualTo(senderCustomerId);
        assertThat(row.get("recipient_customer_id")).isEqualTo(recipientCustomerId);
        assertThat(((BigDecimal) row.get("amount"))).isEqualByComparingTo(new BigDecimal("10.50"));
        assertThat(row.get("status")).isEqualTo(TransferStatus.ACCEPTED.name());
        assertThat(((Timestamp) row.get("created_at")).toInstant()).isEqualTo(record.createdAt());
    }

    @Test
    @DisplayName(value = "Should find transfer audit record by transferId when record exists")
    void findByTransferId_shouldReturnTransferAuditRecordWhenRecordExists() {
        UUID transferId = UUID.randomUUID();
        UUID senderCustomerId = UUID.randomUUID();
        UUID recipientCustomerId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("999.99");
        Instant createdAt = Instant.parse("2026-01-21T10:11:12Z");

        this.namedParameterJdbcTemplate.update(
                """
                INSERT INTO transfers.transfer_operations (
                    transfer_id, sender_customer_id, recipient_customer_id, amount, status, created_at
                ) VALUES (
                    :transferId, :senderCustomerId, :recipientCustomerId, :amount, :status, :createdAt
                )
                """,
                Map.of(
                        "transferId", transferId,
                        "senderCustomerId", senderCustomerId,
                        "recipientCustomerId", recipientCustomerId,
                        "amount", amount,
                        "status", TransferStatus.REJECTED.name(),
                        "createdAt", Timestamp.from(createdAt)
                )
        );

        Optional<TransferAuditRecord> result = this.jdbcTransferRepository.findByTransferId(transferId);

        assertThat(result).isPresent();
        assertThat(result.get().transferId()).isEqualTo(transferId);
        assertThat(result.get().senderCustomerId()).isEqualTo(senderCustomerId);
        assertThat(result.get().recipientCustomerId()).isEqualTo(recipientCustomerId);
        assertThat(result.get().amount()).isEqualByComparingTo(amount);
        assertThat(result.get().status()).isEqualTo(TransferStatus.REJECTED);
        assertThat(result.get().createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName(value = "Should return empty when finding by transferId and record does not exist")
    void findByTransferId_shouldReturnEmptyWhenRecordDoesNotExist() {
        UUID transferId = UUID.randomUUID();

        Optional<TransferAuditRecord> result = this.jdbcTransferRepository.findByTransferId(transferId);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName(value = "Should fail when saving transfer audit record and transferId already exists")
    void save_shouldFailWhenTransferIdAlreadyExists() {
        UUID transferId = UUID.randomUUID();
        UUID senderCustomerId = UUID.randomUUID();
        UUID recipientCustomerId = UUID.randomUUID();

        TransferAuditRecord firstRecord = TransferAuditRecord.accepted(
                transferId,
                senderCustomerId,
                recipientCustomerId,
                new BigDecimal("1.00")
        );

        TransferAuditRecord secondRecord = TransferAuditRecord.rejected(
                transferId,
                senderCustomerId,
                recipientCustomerId,
                new BigDecimal("2.00")
        );

        this.jdbcTransferRepository.save(firstRecord);

        assertThatThrownBy(() -> this.jdbcTransferRepository.save(secondRecord))
                .isInstanceOf(DuplicateKeyException.class);
    }

}
