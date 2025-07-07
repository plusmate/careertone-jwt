package com.careertone.domain.user.controller;

import com.careertone.domain.user.dto.LoginRequest;
import com.careertone.domain.user.dto.LoginResponse;
import com.careertone.domain.user.dto.SignupRequest;
import com.careertone.domain.user.dto.SignupResponse;
import com.careertone.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원가입, 로그인, 관리자 권한 부여 API")
public class UserController {

    private final UserService userServ;

    @Operation(summary = "회원가입", description = "일반 사용자의 회원가입을 처리합니다.")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest req) {
        return ResponseEntity.ok().body(userServ.signup(req));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인하면 JWT 토큰을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok().body(userServ.login(req));
    }

    @Operation(summary = "관리자 회원가입", description = "관리자 권한을 가진 사용자를 등록합니다.")
    @PostMapping("/admin")
    public ResponseEntity<SignupResponse> createAdmin(@RequestBody SignupRequest req) {
        return ResponseEntity.ok().body(userServ.signupWithAdmin(req));
    }

    @Operation(summary = "관리자 권한 부여", description = "기존 사용자에게 관리자 권한을 부여합니다.")
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<SignupResponse> grantPermission(
            @Parameter(description = "권한을 부여할 사용자 ID") @PathVariable Long userId) {
        return ResponseEntity.ok().body(userServ.grantPermission(userId));
    }
}
