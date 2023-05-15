package com.example.service;

import com.example.dto.EntryDto.Response;
import com.example.dto.EntryDto.Request;
import com.example.dto.EntryDto.UpdateResponse;

public interface EntryService {
    Response create(Long applicationId, Request request);
    Response get(Long applicationId);
    UpdateResponse update(Long entryId, Request request);
    void delete(Long entryId);
}
