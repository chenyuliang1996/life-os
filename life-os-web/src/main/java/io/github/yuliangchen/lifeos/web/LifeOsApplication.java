package io.github.yuliangchen.lifeos.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "io.github.yuliangchen.lifeos")
@EntityScan(basePackages = "io.github.yuliangchen.lifeos")
@EnableJpaRepositories(basePackages = "io.github.yuliangchen.lifeos")
@EnableScheduling
/**
 * Life OS Web 启动入口。
 * Spring Boot entry point for Life OS web application.
 */
public class LifeOsApplication {

    /**
     * 应用主函数。
     * Application main method.
     */
    public static void main(String[] args) {
        SpringApplication.run(LifeOsApplication.class, args);
    }
}
