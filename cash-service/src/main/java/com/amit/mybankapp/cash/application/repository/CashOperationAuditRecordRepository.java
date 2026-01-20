package com.amit.mybankapp.cash.application.repository;

import com.amit.mybankapp.cash.model.CashOperationAuditRecord;

public interface CashOperationAuditRecordRepository {

    void save(CashOperationAuditRecord cashOperationAuditRecord);

}
