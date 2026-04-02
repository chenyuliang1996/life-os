package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.SecurityAuditEntry;
import io.github.yuliangchen.lifeos.domain.model.SecurityOverview;
import io.github.yuliangchen.lifeos.web.security.LifeOsSecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/security")
public class SecurityController {

    private final LifeOsSecurityService lifeOsSecurityService;

    public SecurityController(LifeOsSecurityService lifeOsSecurityService) {
        this.lifeOsSecurityService = lifeOsSecurityService;
    }

    @GetMapping("/overview")
    public SecurityOverview overview(@RequestParam(defaultValue = "lifeos-user") String userId,
                                     @RequestParam(defaultValue = "8") int limit) {
        return lifeOsSecurityService.overview(userId, limit);
    }

    @GetMapping("/audit")
    public List<SecurityAuditEntry> audit(@RequestParam(required = false) String userId,
                                          @RequestParam(defaultValue = "12") int limit) {
        return lifeOsSecurityService.recentAudit(userId, limit);
    }
}
