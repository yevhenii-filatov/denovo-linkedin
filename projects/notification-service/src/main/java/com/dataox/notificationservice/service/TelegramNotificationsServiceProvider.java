package com.dataox.notificationservice.service;

import com.dataox.notificationservice.configuration.NotificationsProperties;
import com.dataox.okhttputils.OkHttpTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service("Telegram-notifications")
public class TelegramNotificationsServiceProvider implements NotificationsServiceProvider {
    private static final String REQUEST_URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
    private final NotificationsProperties notificationsProperties;
    private final OkHttpTemplate okHttpTemplate;

    @Override
    public void send(String message) {
        String requestUrl = prepareRequestUrl(message);
        try {
            okHttpTemplate.get(requestUrl);
        } catch (IOException e) {
            log.error("Failed to send telegram message: {}", message);
            log.error("Due to: {} {}", e.getMessage(), e.getClass().getName());
        }
    }

    private String prepareRequestUrl(String message) {
        String token = notificationsProperties.getTelegram().getBotToken();
        String chatId = notificationsProperties.getTelegram().getChatId();
        return String.format(REQUEST_URL, token, chatId, message);
    }
}
