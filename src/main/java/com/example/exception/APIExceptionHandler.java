package com.example.exception;

import com.example.dto.ResponseDto;
import com.example.dto.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler extends RuntimeException{

    @ExceptionHandler(BaseException.class)
    protected ResponseDto<ResultObject> handleBaseException(
            BaseException e,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        log.error(e.getMessage(), e);
        return new ResponseDto<ResultObject>(e);
    }
}
