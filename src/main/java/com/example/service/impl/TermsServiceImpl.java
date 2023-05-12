package com.example.service.impl;

import com.example.domain.Terms;
import com.example.dto.TermsDto.Request;
import com.example.dto.TermsDto.Response;
import com.example.repository.TermsRepository;
import com.example.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        Terms terms = modelMapper.map(request, Terms.class);
        Terms result = termsRepository.save(terms);
        return modelMapper.map(result,Response.class);
    }

    @Override
    public List<Response> getAll() {
        List<Terms> termsList = termsRepository.findAll();
        return termsList.stream()
                .map(terms -> modelMapper.map(terms, Response.class))
                .collect(Collectors.toList());
    }
}
