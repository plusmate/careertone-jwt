package com.careertone.domain.user.entity;

import com.careertone.domain.enums.Role;
import com.careertone.domain.user.dto.SignupRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private Role role;

    public User(SignupRequest signupReq) {
        this.nickname = signupReq.getNickname();
        this.password = signupReq.getPassword();
        this.username = signupReq.getUsername();
        this.role = Role.USER;
    }

    public User grantPermission() {
        this.role = Role.ADMIN;
        return this;
    }

    public User createAdmin(SignupRequest signupReq) {
        this.nickname = signupReq.getNickname();
        this.password = signupReq.getPassword();
        this.username = signupReq.getUsername();
        this.role = Role.ADMIN;
        return this;
    }
}
