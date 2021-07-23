package com.dataox.googleserp.configuration;

import com.dataox.okhttputils.OkHttpTemplate;
import com.dataox.okhttputils.SimpleOkHttpTemplate;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Yevhenii Filatov
 * @since 1/20/21
 */

@Configuration
public class NetworkConfiguration {
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
