package io.github.yuliangchen.lifeos.web.controller;

import io.github.yuliangchen.lifeos.domain.model.SimulatedPersona;
import io.github.yuliangchen.lifeos.web.security.LifeOsSecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personas")
public class PersonaController {

    private final LifeOsSecurityService lifeOsSecurityService;

    public PersonaController(LifeOsSecurityService lifeOsSecurityService) {
        this.lifeOsSecurityService = lifeOsSecurityService;
    }

    @GetMapping
    public List<SimulatedPersona> personas(@RequestParam(defaultValue = "zh-CN") String locale) {
        return lifeOsSecurityService.personas(locale);
    }
}
