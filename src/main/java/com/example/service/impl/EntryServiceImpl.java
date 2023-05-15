package com.example.service.impl;

import com.example.domain.Application;
import com.example.domain.Entry;
import com.example.dto.BalanceDto;
import com.example.dto.EntryDto;
import com.example.exception.BaseException;
import com.example.exception.ResultType;
import com.example.repository.ApplicationRepository;
import com.example.repository.EntryRepository;
import com.example.service.BalanceService;
import com.example.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final BalanceService balanceService;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public EntryDto.Response create(Long applicationId, EntryDto.Request request) {
        if(!isContractApplication(applicationId)){
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);
        entryRepository.save(entry);


        // 대출 잔액 관리
        balanceService.create(applicationId,
                BalanceDto.Request.builder()
                        .entryAmount(request.getEntryAmount())
                        .build()
        );

        return modelMapper.map(entry, EntryDto.Response.class);
    }

    @Override
    public EntryDto.Response get(Long applicationId) {
        Optional<Entry> entry = entryRepository.findByApplicationId(applicationId);
        if(entry.isPresent()){
            return modelMapper.map(entry, EntryDto.Response.class);
        }else{
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public EntryDto.UpdateResponse update(Long entryId, EntryDto.Request request) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        BigDecimal beforeEntryAmount = entry.getEntryAmount();
        entry.setEntryAmount(request.getEntryAmount());
        entryRepository.save(entry);

        Long applicationId = entry.getApplicationId();
        balanceService.update(applicationId,
                BalanceDto.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .build()
        );

        return EntryDto.UpdateResponse.builder()
                .entryId(entryId)
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build();
    }

    @Override
    public void delete(Long entryId) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });
        entry.setIsDeleted(true);
        entryRepository.save(entry);

        BigDecimal beforeEntryAmount = entry.getEntryAmount();

        Long applicationId = entry.getApplicationId();
        balanceService.update(applicationId,
                BalanceDto.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build()
        );
    }

    private boolean isContractApplication(Long applicationId){
        Optional<Application> existed = applicationRepository.findById(applicationId);
        if(existed.isEmpty()){
            return false;
        }

        return existed.get().getContractedAt() != null;
    }
}
