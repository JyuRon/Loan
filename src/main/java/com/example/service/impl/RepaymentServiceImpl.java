package com.example.service.impl;

import com.example.domain.Application;
import com.example.domain.Entry;
import com.example.domain.Repayment;
import com.example.dto.BalanceDto;
import com.example.dto.RepaymentDto;
import com.example.exception.BaseException;
import com.example.exception.ResultType;
import com.example.repository.ApplicationRepository;
import com.example.repository.EntryRepository;
import com.example.repository.RepaymentRepository;
import com.example.service.BalanceService;
import com.example.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;
    private final BalanceService balanceService;
    private final EntryRepository entryRepository;
    private final ModelMapper modelMapper;


    @Override
    public RepaymentDto.Response create(Long applicationId, RepaymentDto.Request request) {
        if(!isRepayableApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment repayment = modelMapper.map(request, Repayment.class);
        repayment.setApplicationId(applicationId);

        repaymentRepository.save(repayment);

        BalanceDto.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDto.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(BalanceDto.RepaymentRequest.RepaymentType.REMOVE)
                        .build()
        );

        RepaymentDto.Response response = modelMapper.map(repayment, RepaymentDto.Response.class);
        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    @Override
    public List<RepaymentDto.ListResponse> get(Long applicationId) {
        List<Repayment> repaymentList = repaymentRepository.findAllByApplicationId(applicationId);
        return repaymentList.stream()
                .map(r -> modelMapper.map(r, RepaymentDto.ListResponse.class))
                .collect(Collectors.toList())
                ;
    }

    @Override
    public RepaymentDto.UpdateResponse update(Long repaymentId, RepaymentDto.Request request) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
                BalanceDto.RepaymentRequest.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(BalanceDto.RepaymentRequest.RepaymentType.ADD)
                        .build()
        );

        repayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(repayment);

        BalanceDto.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                BalanceDto.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(BalanceDto.RepaymentRequest.RepaymentType.REMOVE)
                        .build()
        );


        return RepaymentDto.UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(request.getRepaymentAmount())
                .balance(updatedBalance.getBalance())
                .createdAt(repayment.getCreatedAt())
                .updatedAt(repayment.getUpdatedAt())
                .build();
    }

    @Override
    public void delete(Long repaymentId) {
        Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = repayment.getApplicationId();
        BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
                BalanceDto.RepaymentRequest.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(BalanceDto.RepaymentRequest.RepaymentType.ADD)
                        .build()
        );

        repayment.setIsDeleted(true);
        repaymentRepository.save(repayment);
    }

    private boolean isRepayableApplication(Long applicationId){
        Optional<Application> existedApplication = applicationRepository.findById(applicationId);
        if(existedApplication.isEmpty()){
            return false;
        }

        if(existedApplication.get().getContractedAt() == null){
            return false;
        }

        Optional<Entry> existedEntry = entryRepository.findByApplicationId(applicationId);
        return existedEntry.isPresent();


    }
}
