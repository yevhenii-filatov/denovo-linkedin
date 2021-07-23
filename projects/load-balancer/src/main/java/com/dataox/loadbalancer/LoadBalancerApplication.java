package com.dataox.loadbalancer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@SpringBootApplication(scanBasePackages = "com.dataox")
@EnableJpaRepositories(basePackages = "com.dataox.loadbalancer")
public class LoadBalancerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoadBalancerApplication.class, args);
    }
}
