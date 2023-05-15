package com.example.service;

import com.example.dto.BalanceDto;

public interface BalanceService {
    BalanceDto.Response create(Long applicationId, BalanceDto.Request request);
    BalanceDto.Response update(Long applicationId, BalanceDto.UpdateRequest request);
}
