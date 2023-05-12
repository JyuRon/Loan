package com.example.controller;

import com.example.dto.CounselDto.Response;
import com.example.dto.CounselDto.Request;
import com.example.dto.ResponseDto;
import com.example.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsels")
public class CounselController extends AbstractController{

    private final CounselService counselService;

    @PostMapping
    public ResponseDto<Response> create(@RequestBody Request request){
        return ok(counselService.create(request));
    }

    @GetMapping("/{counselId}")
    public ResponseDto<Response> get(@PathVariable Long counselId){
        return ok(counselService.get(counselId));
    }

    @PutMapping("/{counselId}")
    public ResponseDto<Response> update(
            @PathVariable Long counselId,
            @RequestBody Request request
    ){
        return ok(counselService.update(counselId, request));
    }

    @DeleteMapping("/{counselId}")
    public ResponseDto delete(@PathVariable Long counselId){
        counselService.delete(counselId);
        return ok();
    }
}
