package com.amit.mybankapp.cash.api.mapper;

import com.amit.mybankapp.cash.api.dto.CashOperationResponse;
import com.amit.mybankapp.cash.application.model.CashResult;
import org.springframework.stereotype.Component;

@Component
public class CashOperationMapper {

    public CashOperationResponse toCashOperationResponse(CashResult result) {
        return new CashOperationResponse(
                result.operationId(),
                result.status()
        );
    }

}
