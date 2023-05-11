package com.example.controller;

import com.example.dto.ResponseDto;
import com.example.dto.ResultObject;

public abstract class AbstractController {

    protected <T> ResponseDto<T> ok(){
        return ok(null, ResultObject.getSuccess());
    }

    protected <T> ResponseDto<T> ok(T data){
        return ok(data, ResultObject.getSuccess());
    }

    protected <T> ResponseDto<T> ok(T data, ResultObject result){
        ResponseDto<T> obj = new ResponseDto<>();
        obj.setResult(result);
        obj.setData(data);

        return obj;
    }

}
