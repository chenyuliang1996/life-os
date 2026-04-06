package io.github.yuliangchen.lifeos.domain.model;

public record AuthLoginRequest(
        String username,
        String password
) {
}

