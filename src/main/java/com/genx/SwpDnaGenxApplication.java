package com.genx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.genx.repository")
@SpringBootApplication
public class SwpDnaGenxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwpDnaGenxApplication.class, args);
    }

}
