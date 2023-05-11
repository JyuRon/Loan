package com.example.dto;

import com.example.exception.BaseException;
import com.example.exception.ResultType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultObject {

    public String code;
    public String desc;

    public ResultObject(ResultType resultType){
        this.code = resultType.getCode();
        this.desc = resultType.getDesc();
    }

    public ResultObject(ResultType resultType, String message){
        this.code = resultType.getCode();
        this.desc = message;
    }

    public ResultObject(BaseException e){
        this.code = e.getCode();
        this.desc = e.getDesc();
    }

    public static ResultObject getSuccess(){
        return new ResultObject(ResultType.SUCCESS);
    }

}
