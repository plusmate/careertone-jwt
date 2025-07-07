package com.careertone.domain.user.dto;

import com.careertone.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SignupResponse {
    private String username;
    private String nickname;
    private List<RoleResponse> roles;

    public SignupResponse(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        roles = List.of(new RoleResponse(user.getRole().name()));
    }
}
