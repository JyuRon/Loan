package com.example.service.impl;

import com.example.domain.Balance;
import com.example.dto.BalanceDto;
import com.example.exception.BaseException;
import com.example.exception.ResultType;
import com.example.repository.BalanceRepository;
import com.example.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final ModelMapper modelMapper;

    @Override
    public BalanceDto.Response create(Long applicationId, BalanceDto.Request request) {
        Balance balance = modelMapper.map(request, Balance.class);

        BigDecimal entryAmount = request.getEntryAmount();
        balance.setApplicationId(applicationId);
        balance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(b -> {
            balance.setBalanceId(b.getBalanceId());
            balance.setIsDeleted(b.getIsDeleted());
            balance.setCreatedAt(b.getCreatedAt());
            balance.setUpdatedAt(b.getUpdatedAt());
        });

        Balance result = balanceRepository.save(balance);

        return modelMapper.map(result, BalanceDto.Response.class);
    }

    @Override
    public BalanceDto.Response get(Long applicationId) {
        return null;
    }

    @Override
    public BalanceDto.Response update(Long applicationId, BalanceDto.UpdateRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> {throw new BaseException(ResultType.SYSTEM_ERROR);});

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = balance.getBalance();

        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
        balance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, BalanceDto.Response.class);
    }

    @Override
    public BalanceDto.Response repaymentUpdate(Long applicationId, BalanceDto.RepaymentRequest request) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal updatedBalance = balance.getBalance();
        BigDecimal repaymentAmount = request.getRepaymentAmount();

        if(request.getType().equals(BalanceDto.RepaymentRequest.RepaymentType.ADD)){
            updatedBalance = updatedBalance.add(repaymentAmount);
        }else{
            updatedBalance = updatedBalance.subtract(repaymentAmount);
        }

        balance.setBalance(updatedBalance);
        Balance updated = balanceRepository.save(balance);

        return modelMapper.map(updated, BalanceDto.Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Balance balance = balanceRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> {throw new BaseException(ResultType.SYSTEM_ERROR);});

        balance.setIsDeleted(true);

        balanceRepository.save(balance);
    }
}
