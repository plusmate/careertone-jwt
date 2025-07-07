package com.careertone.domain.user.controller;

import com.careertone.domain.user.dto.LoginRequest;
import com.careertone.domain.user.dto.LoginResponse;
import com.careertone.domain.user.dto.SignupRequest;
import com.careertone.domain.user.dto.SignupResponse;
import com.careertone.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userServ;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest req) {
        return ResponseEntity.ok().body(userServ.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok().body(userServ.login(req));
    }

    /* 어드민 회원가입 */
    @PostMapping("/admin")
    public ResponseEntity<SignupResponse> createAdmin(@RequestBody SignupRequest req) {
        return ResponseEntity.ok().body(userServ.signupWithAdmin(req));
    }

    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<SignupResponse> grantPermission(@PathVariable Long userId) {
        return ResponseEntity.ok().body(userServ.grantPermission(userId));
    }
}
