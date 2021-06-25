package com.dataox.loadbalancer.configuration;

import com.dataox.okhttputils.OkHttpTemplate;
import com.dataox.okhttputils.SimpleOkHttpTemplate;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Configuration
public class HttpRequestsConfiguration {

    @Bean
    public OkHttpTemplate okHttpTemplate(OkHttpClient okHttpClient) {
        return new SimpleOkHttpTemplate(okHttpClient);
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .callTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .followSslRedirects(true)
                .followRedirects(true)
                .build();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
