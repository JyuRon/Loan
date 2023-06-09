package com.example.service;

import com.example.dto.ApplicationDto.GrantAmount;
import com.example.dto.JudgementDto.Response;
import com.example.dto.JudgementDto.Request;

public interface JudgementService {
    Response create(Request request);
    Response get(Long judgementId);
    Response getJudgementOfApplication(Long applicationId);
    Response update(Long judgementId, Request request);
    void delete(Long judgementId);
    GrantAmount grant(Long judgementId);
}
