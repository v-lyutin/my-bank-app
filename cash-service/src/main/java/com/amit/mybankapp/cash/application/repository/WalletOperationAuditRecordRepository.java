package com.amit.mybankapp.cash.application.repository;

import com.amit.mybankapp.cash.model.WalletOperationAuditRecord;

public interface WalletOperationAuditRecordRepository {

    void save(WalletOperationAuditRecord walletOperationAuditRecord);

}
