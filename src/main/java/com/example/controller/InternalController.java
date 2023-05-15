package com.example.controller;

import com.example.dto.EntryDto;
import com.example.dto.RepaymentDto;
import com.example.dto.ResponseDto;
import com.example.service.EntryService;
import com.example.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController{

    private final EntryService entryService;
    private final RepaymentService repaymentService;

    @PostMapping("/{applicationId}/entries")
    public ResponseDto<EntryDto.Response> create(
            @PathVariable Long applicationId,
            @RequestBody EntryDto.Request request
    ){
        return ok(entryService.create(applicationId, request));
    }

    @GetMapping("/{applicationId}/entries")
    public ResponseDto<EntryDto.Response> get(@PathVariable Long applicationId){
        return ok(entryService.get(applicationId));
    }

    @PutMapping("/entries/{entryId}")
    public ResponseDto<EntryDto.UpdateResponse> update(
            @PathVariable Long entryId,
            @RequestBody EntryDto.Request request)
    {
        return ok(entryService.update(entryId, request));
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseDto<Void> delete(@PathVariable Long entryId){
        entryService.delete(entryId);
        return ok();
    }

    @PostMapping("/{applicationId}/repayments")
    public ResponseDto<RepaymentDto.Response> create(
            @PathVariable Long applicationId,
            @RequestBody RepaymentDto.Request request
    ){
        return ok(repaymentService.create(applicationId, request));
    }

    @GetMapping("/{applicationId}/repayments")
    public ResponseDto<List<RepaymentDto.ListResponse>> getPayments(@PathVariable Long applicationId){
        return ok(repaymentService.get(applicationId));
    }

    @PutMapping("/repayments/{repaymentId}")
    public ResponseDto<RepaymentDto.UpdateResponse> update(
            @PathVariable Long repaymentId,
            @RequestBody RepaymentDto.Request request
    ){
        return ok(repaymentService.update(repaymentId, request));
    }

    @DeleteMapping("/repayments/{repaymentId}")
    public ResponseDto<Void> update(@PathVariable Long repaymentId){
        repaymentService.delete(repaymentId);
        return ok();
    }

}
