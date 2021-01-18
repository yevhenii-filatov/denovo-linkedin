package com.dataoxx.scrapingutils.configuration;

import com.dataoxx.scrapingutils.network.OkHttpTemplate;
import com.dataoxx.scrapingutils.network.SimpleOkHttpTemplate;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Yevhenii Filatov
 * @since 1/15/21
 */

@Configuration
public class NetworkConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public OkHttpTemplate okHttpTemplate (OkHttpClient okHttpClient) {
        return new SimpleOkHttpTemplate(okHttpClient);
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .followRedirects(true)
                .build();
    }
}
