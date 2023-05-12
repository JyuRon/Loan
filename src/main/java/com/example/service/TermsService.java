package com.example.service;

import com.example.dto.TermsDto.Request;
import com.example.dto.TermsDto.Response;

import java.util.List;

public interface TermsService {
    Response create(Request request);
    List<Response> getAll();
}
