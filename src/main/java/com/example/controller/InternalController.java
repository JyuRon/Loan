package com.example.controller;

import com.example.dto.EntryDto;
import com.example.dto.ResponseDto;
import com.example.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController{

    private final EntryService entryService;

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

}
