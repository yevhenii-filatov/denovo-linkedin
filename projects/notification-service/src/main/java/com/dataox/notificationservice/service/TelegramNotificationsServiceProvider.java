package com.dataox.notificationservice.service;

import com.dataox.notificationservice.configuration.NotificationsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service("Telegram-notifications")
public class TelegramNotificationsServiceProvider implements NotificationsServiceProvider {
    private static final String REQUEST_URL = "https://api.telegram.org/bot{bot_token}/sendMessage?chat_id={chat_id}&text={text}";
    private final NotificationsProperties notificationsProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void send(String message) {
        NotificationsProperties.Telegram telegram = notificationsProperties.getTelegram();
        try {
            restTemplate.getForObject(REQUEST_URL, String.class,
                    telegram.getBotToken(),
                    telegram.getChatId(),
                    message);
            log.info("Sent message into telegram");
        } catch (RestClientException e) {
            log.error("Failed to send telegram message: {}", message);
            log.error("Exception message: {}", e.getMessage());
        }
    }

}
