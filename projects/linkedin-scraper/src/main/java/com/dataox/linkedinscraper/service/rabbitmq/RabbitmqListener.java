package com.dataox.linkedinscraper.service.rabbitmq;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.service.ScrapeLinkedinProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 03/03/2021
 */
@Service
@RequiredArgsConstructor
@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
public class RabbitmqListener {
    private final ScrapeLinkedinProfileService scrapeLinkedinProfileService;

    @RabbitHandler(isDefault = true)
    public void receiveMessage(List<LinkedinProfileToScrapeDTO> profiles) {
        scrapeLinkedinProfileService.scrape(profiles);
    }
}
