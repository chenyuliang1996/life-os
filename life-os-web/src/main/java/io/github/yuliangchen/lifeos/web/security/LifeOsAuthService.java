package io.github.yuliangchen.lifeos.web.security;

import io.github.yuliangchen.lifeos.domain.model.AuthCurrentUserResponse;
import io.github.yuliangchen.lifeos.domain.model.AuthLoginRequest;
import io.github.yuliangchen.lifeos.domain.model.AuthLoginResponse;
import io.github.yuliangchen.lifeos.domain.model.AuthRegisterRequest;
import io.github.yuliangchen.lifeos.domain.model.AuthSession;
import io.github.yuliangchen.lifeos.domain.model.AuthUserAccount;
import io.github.yuliangchen.lifeos.domain.repository.AuthSessionRepository;
import io.github.yuliangchen.lifeos.domain.repository.AuthUserAccountRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
@EnableConfigurationProperties(LifeOsAuthService.AuthProperties.class)
public class LifeOsAuthService {

    private final AuthUserAccountRepository authUserAccountRepository;
    private final AuthSessionRepository authSessionRepository;
    private final LifeOsSecurityService lifeOsSecurityService;
    private final AuthProperties properties;
    private final java.security.SecureRandom secureRandom;

    public LifeOsAuthService(AuthUserAccountRepository authUserAccountRepository,
                             AuthSessionRepository authSessionRepository,
                             LifeOsSecurityService lifeOsSecurityService,
                             AuthProperties properties) {
        this.authUserAccountRepository = authUserAccountRepository;
        this.authSessionRepository = authSessionRepository;
        this.lifeOsSecurityService = lifeOsSecurityService;
        this.properties = properties;
        this.secureRandom = new java.security.SecureRandom();
    }

    public AuthLoginResponse register(AuthRegisterRequest request) {
        if (!properties.registrationEnabledValue()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Registration is disabled");
        }
        String username = normalizeUsername(request.username());
        String password = normalizePassword(request.password());
        if (authUserAccountRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        Instant now = Instant.now();
        AuthUserAccount created = new AuthUserAccount(
                generateUserId(),
                username,
                hashPassword(username, password),
                normalizeDisplayName(request.displayName(), username),
                normalizeLocale(request.locale()),
                true,
                now,
                now
        );
        AuthUserAccount saved = authUserAccountRepository.save(created);
        lifeOsSecurityService.recordAudit(
                saved.userId(),
                "auth-thread",
                "auth",
                "register",
                saved.userId(),
                "SUCCESS",
                "Created account for username " + saved.username()
        );
        return issueSession(saved);
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        String username = normalizeUsername(request.username());
        String password = normalizePassword(request.password());
        AuthUserAccount account = authUserAccountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!account.enabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account disabled");
        }
        if (!Objects.equals(account.passwordHash(), hashPassword(username, password))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        authSessionRepository.deleteExpired();
        Instant now = Instant.now();
        AuthUserAccount updatedAccount = authUserAccountRepository.save(new AuthUserAccount(
                account.userId(),
                account.username(),
                account.passwordHash(),
                account.displayName(),
                account.locale(),
                account.enabled(),
                account.createdAt(),
                now
        ));
        lifeOsSecurityService.recordAudit(
                updatedAccount.userId(),
                "auth-thread",
                "auth",
                "login",
                updatedAccount.userId(),
                "SUCCESS",
                "User logged in"
        );
        return issueSession(updatedAccount);
    }

    public AuthCurrentUserResponse currentUser(String authorizationHeader) {
        AuthUserAccount account = authenticate(authorizationHeader);
        AuthSession session = activeSession(extractToken(authorizationHeader));
        return new AuthCurrentUserResponse(
                account.userId(),
                account.username(),
                account.displayName(),
                account.locale(),
                session.expiresAt(),
                account.lastLoginAt()
        );
    }

    public String resolveUserId(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        return currentUser(authorizationHeader).userId();
    }

    public void logout(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        AuthSession session = activeSession(token);
        authSessionRepository.revoke(token);
        lifeOsSecurityService.recordAudit(
                session.userId(),
                "auth-thread",
                "auth",
                "logout",
                session.userId(),
                "SUCCESS",
                "User session revoked"
        );
    }

    public AuthUserAccount authenticate(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        AuthSession session = activeSession(token);
        AuthUserAccount account = authUserAccountRepository.findByUserId(session.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not found"));
        if (!account.enabled()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account disabled");
        }
        return account;
    }

    private AuthLoginResponse issueSession(AuthUserAccount account) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.sessionTtlHours(), ChronoUnit.HOURS);
        String token = generateToken();
        authSessionRepository.save(new AuthSession(
                token,
                account.userId(),
                now,
                expiresAt,
                now,
                false
        ));
        return new AuthLoginResponse(
                token,
                account.userId(),
                account.username(),
                account.displayName(),
                account.locale(),
                expiresAt
        );
    }

    private AuthSession activeSession(String token) {
        authSessionRepository.deleteExpired();
        AuthSession session = authSessionRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session token"));
        Instant now = Instant.now();
        if (session.revoked() || session.expiresAt().isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session expired or revoked");
        }
        AuthSession refreshed = new AuthSession(
                session.token(),
                session.userId(),
                session.issuedAt(),
                session.expiresAt(),
                now,
                false
        );
        authSessionRepository.save(refreshed);
        return refreshed;
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is required");
        }
        String trimmed = authorizationHeader.trim();
        if (!trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer token is required");
        }
        String token = trimmed.substring(7).trim();
        if (token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bearer token is required");
        }
        return token;
    }

    private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        String normalized = username.trim().toLowerCase(Locale.ROOT);
        if (normalized.length() < 3 || normalized.length() > 32) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username length must be 3-32");
        }
        return normalized;
    }

    private String normalizePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }
        String normalized = password.trim();
        if (normalized.length() < 6 || normalized.length() > 128) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password length must be 6-128");
        }
        return normalized;
    }

    private String normalizeDisplayName(String displayName, String username) {
        if (displayName == null || displayName.isBlank()) {
            return username;
        }
        return displayName.trim();
    }

    private String normalizeLocale(String locale) {
        if (locale == null || locale.isBlank()) {
            return "zh-CN";
        }
        return locale.trim();
    }

    private String generateUserId() {
        return "user-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return "otk_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashPassword(String username, String rawPassword) {
        String payload = username + ":" + rawPassword + ":" + properties.passwordPepper();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", exception);
        }
    }

    @ConfigurationProperties(prefix = "lifeos.auth")
    public record AuthProperties(
            long sessionTtlHours,
            String passwordPepper,
            Boolean registrationEnabled
    ) {

        public AuthProperties {
            if (sessionTtlHours <= 0) {
                sessionTtlHours = 72;
            }
            if (passwordPepper == null || passwordPepper.isBlank()) {
                passwordPepper = "life-os-local-pepper";
            }
        }

        public boolean registrationEnabledValue() {
            return registrationEnabled == null || registrationEnabled;
        }
    }
}
