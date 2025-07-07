package com.careertone.jwt;

import com.careertone.exception.ApplicationException;
import com.careertone.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private final long validityInMilliseconds = 1000 * 60 * 60 * 2; // 2 hours

    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Long userId, String username, Set<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        List<String> roles = getRoles(token);
        return new UsernamePasswordAuthenticationToken(
                username,
                "",
                roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList())
        );
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return (List<String>) claims.get("roles");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN);
        }
    }
}
