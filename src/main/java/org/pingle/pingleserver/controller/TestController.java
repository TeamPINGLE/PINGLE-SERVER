package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.domain.enums.URole;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final JwtUtil jwtUtil;
    @GetMapping("/token")
    public JwtTokenResponse testToken() {
        return jwtUtil.generateTokens(1L, URole.ADMIN);
    }

    @GetMapping("/user-test")
    public ResponseEntity<Long> testUser(@UserId Long userId) {
        return ResponseEntity.ok(userId);
    }

}
