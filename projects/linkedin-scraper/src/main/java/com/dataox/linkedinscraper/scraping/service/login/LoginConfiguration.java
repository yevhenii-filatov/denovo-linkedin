package com.dataox.linkedinscraper.scraping.service.login;

import com.dataox.linkedinscraper.dto.AccountCredentials;
import com.dataox.okhttputils.OkHttpTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Component
@RequiredArgsConstructor
public class LoginConfiguration {

    private final OkHttpTemplate okHttpTemplate;

    @Bean
    public AccountCredentials accountCredentials() {
        return getAccountCredentialsFromLoadBalancer();
    }

    private AccountCredentials getAccountCredentialsFromLoadBalancer() {
        //perform request to load balancer for user credentials
        return new AccountCredentials("taykhal@mail.ru", "1QYOFqJ5Nw");
    }

    @PreDestroy
    public void releaseUserCredentials() {
        //perform request to load balancer and mark user credentials as free
    }
}
