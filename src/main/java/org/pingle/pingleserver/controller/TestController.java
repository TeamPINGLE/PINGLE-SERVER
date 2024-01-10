package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pingle.pingleserver.annotation.UserId;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.dto.response.JwtTokenResponse;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.repository.UserRepository;
import org.pingle.pingleserver.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/token/{userId}")
    public JwtTokenResponse testToken(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorMessage.USER_NOT_FOUND));
        JwtTokenResponse response = jwtUtil.generateTokens(user.getId(), user.getRole());
        user.updateRefreshToken(response.refreshToken());
        return response;
    }

    @GetMapping("/user-test")
    public ResponseEntity<Long> testUser(@UserId Long userId) {
        return ResponseEntity.ok(userId);
    }

}
