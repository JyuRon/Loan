package com.example.service;

import com.example.dto.ApplicationDto.Response;
import com.example.dto.ApplicationDto.Request;

public interface ApplicationService {

    Response create(Request request);
    Response get(Long applicationId);
    Response update(Long applicationId, Request request);
    void delete(Long applicationId);
}
