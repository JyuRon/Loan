package com.example.dto;

import com.example.exception.BaseException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude // default : 모든 필드를 json 형태로 만든다.
public class ResponseDto<T> implements Serializable {

    private ResultObject result;
    private T data;

    public ResponseDto(ResultObject result){
        this.result = result;
    }

    public ResponseDto(T data){
        this.data = data;
    }

    public static <T> ResponseDto<T> ok(){
        return new ResponseDto<>(ResultObject.getSuccess());
    }

    public static <T> ResponseDto<T> ok(T data){
        return new ResponseDto<>(ResultObject.getSuccess(), data);
    }

    public static <T> ResponseDto<T> response(T data){
        return new ResponseDto<>(ResultObject.getSuccess(), data);
    }

    public ResponseDto(BaseException ex){
        this.result = new ResultObject(ex);
    }
}
