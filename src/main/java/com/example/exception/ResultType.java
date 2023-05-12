package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {

    SUCCESS("0000", "success"),
    NOT_EXIST_FILE("4001", "file not exist"),
    SYSTEM_ERROR("9000", "system error"),
    ;

    private final String code;
    private final String desc;
}
