package com.careertone.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginResponse {
    private String token;
    public LoginResponse(String token) {
        this.token = token;
    }
}
