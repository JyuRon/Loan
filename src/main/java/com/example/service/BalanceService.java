package com.example.service;

import com.example.dto.BalanceDto;

public interface BalanceService {
    BalanceDto.Response create(Long applicationId, BalanceDto.Request request);
    BalanceDto.Response get(Long applicationId);
    BalanceDto.Response update(Long applicationId, BalanceDto.UpdateRequest request);
    BalanceDto.Response repaymentUpdate(Long applicationId, BalanceDto.RepaymentRequest request);
    void delete(Long applicationId);
}
