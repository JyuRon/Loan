package com.example.service.impl;

import com.example.domain.Judgement;
import com.example.dto.JudgementDto.Response;
import com.example.dto.JudgementDto.Request;
import com.example.exception.BaseException;
import com.example.exception.ResultType;
import com.example.repository.ApplicationRepository;
import com.example.repository.JudgementRepository;
import com.example.service.JudgementService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JudgementServiceImpl implements JudgementService {

    private final JudgementRepository judgementRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;
    @Override
    public Response create(Request request) {
        Long applicationId = request.getApplicationId();;
        if(!isPresentApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgement judgement = modelMapper.map(request, Judgement.class);
        Judgement result = judgementRepository.save(judgement);

        return modelMapper.map(result, Response.class);
    }

    @Override
    public Response get(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId).orElseThrow(
                () -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });
        return modelMapper.map(judgement, Response.class);
    }

    @Override
    public Response getJudgementOfApplication(Long applicationId) {
        if(!isPresentApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgement judgement = judgementRepository.findByApplicationId(applicationId).orElseThrow(
                () -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                }
        );
        return modelMapper.map(judgement, Response.class);
    }

    @Override
    public Response update(Long judgementId, Request request) {
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        judgement.setName(request.getName());
        judgement.setApprovalAmount(request.getApprovalAmount());

        judgementRepository.save(judgement);

        return modelMapper.map(judgement, Response.class);
    }

    @Override
    public void delete(Long judgementId) {
        Judgement judgement = judgementRepository.findById(judgementId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        judgement.setIsDeleted(true);
        judgementRepository.save(judgement);
    }

    private boolean isPresentApplication(Long applicationId){
        return applicationRepository.findById(applicationId).isPresent();
    }
}
