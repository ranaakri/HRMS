package com.mycompany.hrms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.mycompany.hrms")
@EnableJpaRepositories(basePackages = "com.mycompany.hrms.data.repository")
@EntityScan(basePackages = "com.mycompany.hrms.data.entity")
@RestController
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}