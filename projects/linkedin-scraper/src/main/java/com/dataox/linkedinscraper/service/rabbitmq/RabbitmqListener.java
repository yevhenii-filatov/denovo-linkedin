package com.dataox.linkedinscraper.service.rabbitmq;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.linkedinscraper.service.ScrapeLinkedinProfileService;
import com.dataox.okhttputils.OkHttpTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 03/03/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
public class RabbitmqListener {
    private final ScrapeLinkedinProfileService scrapeLinkedinProfileService;
    private final OkHttpTemplate okHttpTemplate;
    private final ObjectMapper objectMapper;

    @RabbitHandler(isDefault = true)
    public void receiveMessage(List<LinkedinProfileToScrapeDTO> profiles) throws IOException, InterruptedException {
        log.info("Received {} profiles to scrape", profiles.size());
        objectMapper.registerModule(new JavaTimeModule());
        ScrapingResultsDTO scrape = scrapeLinkedinProfileService.scrape(profiles);
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/v1/scraping/receive/scraped")
                .method("POST", RequestBody.create(MediaType.get("application/json"),objectMapper.writeValueAsString(scrape)))
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            okHttpTemplate.request(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
