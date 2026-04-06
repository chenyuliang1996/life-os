package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.AuthCurrentUserResponse;
import io.github.yuliangchen.lifeos.domain.model.AuthLoginRequest;
import io.github.yuliangchen.lifeos.domain.model.AuthLoginResponse;
import io.github.yuliangchen.lifeos.domain.model.AuthRegisterRequest;
import io.github.yuliangchen.lifeos.web.security.LifeOsAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LifeOsAuthService lifeOsAuthService;

    public AuthController(LifeOsAuthService lifeOsAuthService) {
        this.lifeOsAuthService = lifeOsAuthService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthLoginResponse register(@RequestBody AuthRegisterRequest request) {
        return lifeOsAuthService.register(request);
    }

    @PostMapping("/login")
    public AuthLoginResponse login(@RequestBody AuthLoginRequest request) {
        return lifeOsAuthService.login(request);
    }

    @GetMapping("/me")
    public AuthCurrentUserResponse me(@RequestHeader("Authorization") String authorizationHeader) {
        return lifeOsAuthService.currentUser(authorizationHeader);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String authorizationHeader) {
        lifeOsAuthService.logout(authorizationHeader);
    }
}
