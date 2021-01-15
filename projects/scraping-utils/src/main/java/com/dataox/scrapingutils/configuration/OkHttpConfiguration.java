package com.dataox.scrapingutils.configuration;

import com.dataox.scrapingutils.network.OkHttpTemplate;
import com.dataox.scrapingutils.network.SimpleOkHttpTemplate;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Yevhenii Filatov
 * @since 1/15/21
 */

@Configuration
public class OkHttpConfiguration {
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
