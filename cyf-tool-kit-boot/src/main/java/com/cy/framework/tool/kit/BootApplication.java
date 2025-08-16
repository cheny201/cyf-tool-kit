package com.cy.framework.tool.kit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

@Slf4j
@EnableCaching
@ServletComponentScan(basePackages = "com.cy")
@SpringBootApplication(scanBasePackages = "com.cy")
public class BootApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BootApplication.class, args);
        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            log.error("Spring Boot 使用profile为:{}", profile);
        }
    }
}
