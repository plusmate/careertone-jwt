package com.careertone.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String nickname;
}
