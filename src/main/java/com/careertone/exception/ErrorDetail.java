package com.careertone.exception;

import lombok.Getter;

@Getter
public class ErrorDetail {
    private final String code;
    private final String message;

    public ErrorDetail(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
