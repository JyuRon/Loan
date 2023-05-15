package com.example.service;

import com.example.dto.RepaymentDto;

import java.util.List;

public interface RepaymentService {

    RepaymentDto.Response create(Long applicationId, RepaymentDto.Request request);
    List<RepaymentDto.ListResponse> get(Long applicationId);
    RepaymentDto.UpdateResponse update(Long repaymentId, RepaymentDto.Request request);
    void delete(Long repaymentId);
}
