package com.careertone.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final ErrorDetail error;

    public ErrorResponse(String code, String message) {
        this.error = new ErrorDetail(code, message);
    }
}
