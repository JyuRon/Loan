package com.example.service.impl;

import com.example.domain.Application;
import com.example.domain.Terms;
import com.example.dto.ApplicationDto.AcceptTerms;
import com.example.dto.ApplicationDto.Request;
import com.example.dto.ApplicationDto.Response;
import com.example.exception.BaseException;
import com.example.exception.ResultType;
import com.example.repository.AcceptTermsRepository;
import com.example.repository.ApplicationRepository;
import com.example.repository.TermsRepository;
import com.example.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final TermsRepository termsRepository;
    private final AcceptTermsRepository acceptTermsRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now());

        Application result = applicationRepository.save(application);

        return modelMapper.map(result, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        return modelMapper.map(application, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        application.setIsDeleted(true);

        applicationRepository.save(application);
    }

    /**
     * 서버가 가지고 있는 약관을 모두 동의 하였을때 통과하도록 가정한다.
     * client 로부터 전달 받은 사용자가 동의한 약관 번호를 가지고 검증을 시도한다.
     */
    @Override
    public Boolean acceptTerms(Long applicationId, AcceptTerms request) {

        // 대출 신청 여부 조회
        applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BaseException(ResultType.SYSTEM_ERROR));

        // 동의가 필요한 약관들 전체 조회
        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if(termsList.isEmpty()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // clinet 로 부터 전달받은 사용자가 동의한 약관
        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if(termsList.size() != acceptTermsIds.size()){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termsList.stream()
                .map(Terms::getTermsId)
                .collect(Collectors.toList());
        Collections.sort(termsIds);

        // 전달받은 약관 내용이 모두 존재하는지 체크
        if(!termsIds.containsAll(acceptTermsIds)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for(Long termsId : acceptTermsIds){
            com.example.domain.AcceptTerms accepted = com.example.domain.AcceptTerms.builder()
                    .applicationId(applicationId)
                    .termsId(termsId)
                    .build();
            acceptTermsRepository.save(accepted);
        }
        return true;
    }
}
