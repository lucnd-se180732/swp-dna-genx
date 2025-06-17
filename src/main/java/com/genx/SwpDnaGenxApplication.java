package com.genx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.genx.repository")
@EntityScan(basePackages = "com.genx")
public class SwpDnaGenxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwpDnaGenxApplication.class, args);
    }

}
