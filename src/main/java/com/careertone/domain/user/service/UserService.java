package com.careertone.domain.user.service;

import com.careertone.domain.enums.Role;
import com.careertone.domain.user.dto.LoginRequest;
import com.careertone.domain.user.dto.LoginResponse;
import com.careertone.domain.user.dto.SignupRequest;
import com.careertone.domain.user.dto.SignupResponse;
import com.careertone.domain.user.entity.User;
import com.careertone.domain.user.repository.UserRepository;
import com.careertone.exception.ApplicationException;
import com.careertone.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.careertone.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new ApplicationException(USER_ALREADY_EXISTS);
        }

        String encodedPW = passwordEncoder.encode(req.getPassword());
        req.setPassword(encodedPW);

        User user = new User(req);
        userRepo.save(user);

        return new SignupResponse(user);
    }

    public SignupResponse signupWithAdmin(SignupRequest req) {
        User user = new User(req);

        String encodedPW = passwordEncoder.encode(req.getPassword());
        req.setPassword(encodedPW);

        User admin = user.createAdmin(req);
        userRepo.save(admin);

        return new SignupResponse(user);
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new ApplicationException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ApplicationException(INVALID_CREDENTIALS);
        }

        String token = jwtTokenProvider.createToken(
                user.getId(),
                user.getUsername(),
                Set.of(user.getRole().name()));
        return new LoginResponse(token);
    }

    public SignupResponse grantPermission(Long userId) {
        String userRole = getUserRole();
        if (userRole.equals(Role.USER.name())) {
            throw new ApplicationException(ACCESS_DENIED);
        }

        return new SignupResponse(findById(userId).grantPermission());
    }

    public String getUserRole() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse(null);
    }

    private User findById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ApplicationException(NOT_FOUND_USER));
    }
}
