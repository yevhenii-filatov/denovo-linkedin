package com.dataox.notificationservice.service;

import com.dataox.notificationservice.configuration.NotificationsProperties;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.InterruptedIOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service("Telegram-notifications")
public class TelegramNotificationsServiceProvider implements NotificationsServiceProvider {
    private static final String REQUEST_URL = "https://api.telegram.org/bot{bot_token}/sendMessage?chat_id={chat_id}&text={text}";
    private final NotificationsProperties notificationsProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void send(String message) {

        if (Objects.isNull(message)) message = "empty";
        if (message.isEmpty()) message = "empty";

        if (!isMessageTooLong(message)) {

            NotificationsProperties.Telegram telegram = notificationsProperties.getTelegram();
            try {
                restTemplate.getForObject(REQUEST_URL, String.class,
                        telegram.getBotToken(),
                        telegram.getChatId(),
                        message);
            } catch (RestClientException e) {
                log.error("Failed to send telegram message: {}", message);
                log.error("Exception message: {}", e.getMessage());
            }
        }

    }

    private boolean isMessageTooLong(String message) {
        if (message.length() > 4000) {
            Iterable<String> chunks = Splitter.fixedLength(4000).split(message);
            List<String> messages = Lists.newArrayList(chunks);
            for (String oneMessage : messages) {
                send(oneMessage);
            }
            return true;
        } else return false;
    }

}
