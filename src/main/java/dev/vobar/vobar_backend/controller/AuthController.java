package dev.vobar.vobar_backend.controller;

import dev.vobar.vobar_backend.dto.LoginRequest;
import dev.vobar.vobar_backend.dto.LoginResponse;
import dev.vobar.vobar_backend.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.info("login was requested");
        return authService.login(request);
    }
}
