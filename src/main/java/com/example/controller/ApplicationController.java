package com.example.controller;

import com.example.dto.ApplicationDto.Request;
import com.example.dto.ApplicationDto.Response;
import com.example.dto.ApplicationDto.AcceptTerms;
import com.example.dto.ResponseDto;
import com.example.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController extends AbstractController{

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseDto<Response> create(@RequestBody Request request){
        return ok(applicationService.create(request));
    }

    @GetMapping("/{applicationId}")
    public ResponseDto<Response> get(@PathVariable Long applicationId){
        return ok(applicationService.get(applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseDto<Response> update(
            @PathVariable Long applicationId,
            @RequestBody Request request
    ){
        return ok(applicationService.update(applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDto delete(@PathVariable Long applicationId){
        applicationService.delete(applicationId);;
        return ok();
    }

    @PostMapping("/{applicationId}/terms")
    public ResponseDto<Boolean> acceptTerms(
            @PathVariable Long applicationId,
            @RequestBody AcceptTerms request
    ){
        return ok(applicationService.acceptTerms(applicationId, request));
    }
}
