package com.careertone.domain.user.controller;

import com.careertone.config.SecurityConfig;
import com.careertone.domain.enums.Role;
import com.careertone.domain.user.dto.*;
import com.careertone.domain.user.service.UserService;
import com.careertone.exception.ApplicationException;
import com.careertone.exception.ErrorCode;
import com.careertone.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtTokenProvider.class})
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService userService;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    private String getToken(Long userId, String username, Role role) {
        return jwtTokenProvider.createToken(userId, username, Set.of(role.name()));
    }

    @Test
    void 회원가입_성공() throws Exception {
        SignupRequest request = new SignupRequest("testuser", "pw1", "닉네임");
        RoleResponse role = new RoleResponse(Role.USER.name());
        SignupResponse response = new SignupResponse("testuser", "닉네임", List.of(role));

        given(userService.signup(any(SignupRequest.class))).willReturn(response);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.roles[0].role").value("USER"));
    }

    @Test
    void 회원가입_실패_중복된_사용자() throws Exception {
        SignupRequest request = new SignupRequest("testuser", "pw1", "닉네임");

        given(userService.signup(any(SignupRequest.class)))
                .willThrow(new ApplicationException(ErrorCode.USER_ALREADY_EXISTS));

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"));
    }

    @Test
    void 로그인_성공() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "pw1");
        LoginResponse response = new LoginResponse("dummy.jwt.token");

        given(userService.login(any(LoginRequest.class))).willReturn(response);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy.jwt.token"));
    }

    @Test
    void 로그인_실패_잘못된_자격증명() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");

        given(userService.login(any(LoginRequest.class)))
                .willThrow(new ApplicationException(ErrorCode.INVALID_CREDENTIALS));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void 권한부여_성공_관리자() throws Exception {
        String token = getToken(1L, "adminUser", Role.ADMIN);

        SignupResponse response = new SignupResponse("targetUser", "닉네임", List.of(new RoleResponse("ADMIN")));
        given(userService.grantPermission(anyLong())).willReturn(response);

        mockMvc.perform(patch("/admin/users/2/roles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("targetUser"))
                .andExpect(jsonPath("$.roles[0].role").value("ADMIN"));
    }

    @Test
    void 권한부여_실패_일반유저() throws Exception {
        String token = getToken(2L, "normalUser", Role.USER);

        given(userService.grantPermission(anyLong()))
                .willThrow(new ApplicationException(ErrorCode.ACCESS_DENIED));

        mockMvc.perform(patch("/admin/users/3/roles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"));
    }

    @Test
    void 권한부여_실패_존재하지않는유저() throws Exception {
        String token = getToken(1L, "adminUser", Role.ADMIN);

        given(userService.grantPermission(anyLong()))
                .willThrow(new ApplicationException(ErrorCode.NOT_FOUND_USER));

        mockMvc.perform(patch("/admin/users/999/roles")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND_USER"));
    }
}
