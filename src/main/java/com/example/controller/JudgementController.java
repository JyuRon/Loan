package com.example.controller;

import com.example.domain.BaseEntity;
import com.example.dto.JudgementDto.Response;
import com.example.dto.JudgementDto.Request;
import com.example.dto.ResponseDto;
import com.example.service.JudgementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.dto.ResponseDto.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/judgements")
public class JudgementController extends BaseEntity {

    private final JudgementService judgementService;

    @PostMapping
    public ResponseDto<Response> create(@RequestBody Request request){
        return ok(judgementService.create(request));
    }

    @GetMapping("/{judgementId}")
    public ResponseDto<Response> get(@PathVariable Long judgementId){
        return ok(judgementService.get(judgementId));
    }

    @GetMapping("/applications/{applicationId}")
    public ResponseDto<Response> getJudgementOfApplication(@PathVariable Long applicationId){
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }

    @PutMapping("/{judgementId}")
    public ResponseDto<Response> update(
            @PathVariable Long judgementId,
            @RequestBody Request request
    ){
        return ok(judgementService.update(judgementId, request));
    }

    @DeleteMapping("/{judgementId}")
    public ResponseDto<Void> delete(@PathVariable Long judgementId){
        judgementService.delete(judgementId);
        return ok();
    }
}

