package org.pingle.pingleserver.controller;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.domain.enums.URole;
import org.pingle.pingleserver.repository.UserRepository;
import org.pingle.pingleserver.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile({"local", "dev"})
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${token-test.key}")
    private String key;

    @GetMapping("/token/{userId}")
    public ResponseEntity<?> getToken(@PathVariable("userId") Long userId,
                                                     @RequestHeader(value = "Test-Key", required = false) String testKey){
        if (!key.isBlank() && !key.equals(testKey)) {
            return ResponseEntity.badRequest().body("Invalid test key");
        }
        userRepository.findByIdOrThrow(userId);
        return ResponseEntity.ok(jwtUtil.generateTokens(userId, URole.USER));
    }
}
