package com.dataox.scrapingutils.notification;

import com.dataox.scrapingutils.configuration.properties.NotificationProperties;
import com.dataox.scrapingutils.network.OkHttpTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Yevhenii Filatov
 * @since 12/30/20
 */

@Slf4j
@Service
@RequiredArgsConstructor
class TelegramNotificationService implements NotificationService {
    private static final String SEND_MESSAGE_UTL_TEMPLATE = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private final NotificationProperties properties;

    private final OkHttpTemplate okHttpTemplate;

    @Override
    public void send(String message) {
        if (properties.getTelegram().getEnabled()) {
            String requestUrl = prepareRequestUrl(message);
            try {
                okHttpTemplate.get(requestUrl);
                log.info("Message '{}' was sent to #{}", message, properties.getTelegram().getChannelId());
            } catch (IOException e) {
                log.error("Failed to send Telegram notification: {}", message);
                log.error("Reason => {}:{}", e.getClass().getName(), e.getMessage());
            }
        }
    }

    private String prepareRequestUrl(String message) {
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        String botToken = properties.getTelegram().getBotToken();
        String channelId = properties.getTelegram().getChannelId();
        return String.format(SEND_MESSAGE_UTL_TEMPLATE, botToken, channelId, encodedMessage);
    }
}
