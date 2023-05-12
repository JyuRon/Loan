package com.example.service.impl;

import com.example.domain.Counsel;
import com.example.dto.CounselDto.Response;
import com.example.dto.CounselDto.Request;
import com.example.exception.BaseException;
import com.example.exception.ResultType;
import com.example.repository.CounselRepository;
import com.example.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {

    private final ModelMapper modelMapper;
    private final CounselRepository counselRepository;

    @Override
    public Response create(Request request) {
        // entity 로 매핑
        Counsel counsel = modelMapper.map(request, Counsel.class);
        counsel.setAppliedAt(LocalDateTime.now());

        // db에 저장된 값을 반환
        Counsel created = counselRepository.save(counsel);


        return modelMapper.map(created, Response.class);
    }

    @Override
    public Response get(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        return modelMapper.map(counsel, Response.class);
    }

    @Override
    public Response update(Long counselId, Request request) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        // TODO : 공백이 들어오면 그 값으로 치환을 하고 있다. validation 이 추가 되어야 한다.
        counsel.setName(request.getName());
        counsel.setCellPhone(request.getCellPhone());
        counsel.setAddress(request.getAddress());
        counsel.setAddressDetail(request.getAddressDetail());
        counsel.setZipCode(request.getZipCode());
        counsel.setMemo(request.getMemo());
        counsel.setEmail(request.getEmail());

        counselRepository.save(counsel);

        return modelMapper.map(counsel, Response.class);
    }

    @Override
    public void delete(Long counselId) {
        Counsel counsel = counselRepository.findById(counselId)
                .orElseThrow(() -> {
                    throw new BaseException(ResultType.SYSTEM_ERROR);
                });

        counsel.setIsDeleted(true);
        counselRepository.save(counsel);
    }
}
