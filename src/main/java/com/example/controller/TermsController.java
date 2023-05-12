package com.example.controller;

import com.example.dto.ResponseDto;
import com.example.dto.TermsDto.Request;
import com.example.dto.TermsDto.Response;
import com.example.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/terms")
public class TermsController extends AbstractController{

    private final TermsService termsService;

    @PostMapping
    public ResponseDto<Response> create(@RequestBody Request request){
        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDto<List<Response>> getAll(){
        return ok(termsService.getAll());
    }
}
