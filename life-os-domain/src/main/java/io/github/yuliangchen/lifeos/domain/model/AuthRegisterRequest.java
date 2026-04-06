package io.github.yuliangchen.lifeos.domain.model;

public record AuthRegisterRequest(
        String username,
        String password,
        String displayName,
        String locale
) {
}

