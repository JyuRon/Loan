package com.example.service;

import com.example.dto.CounselDto.Response;
import com.example.dto.CounselDto.Request;

public interface CounselService {

    Response create(Request request);
    Response get(Long counselId);
    Response update(Long counselId, Request request);
    void delete(Long counselId);
}
