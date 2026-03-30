package io.github.yuliangchen.lifeos.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "io.github.yuliangchen.lifeos")
@EntityScan(basePackages = "io.github.yuliangchen.lifeos")
@EnableJpaRepositories(basePackages = "io.github.yuliangchen.lifeos")
public class LifeOsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeOsApplication.class, args);
    }
}
