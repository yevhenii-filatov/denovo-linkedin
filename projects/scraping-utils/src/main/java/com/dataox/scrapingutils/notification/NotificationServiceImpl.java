package com.dataox.scrapingutils.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author Yevhenii Filatov
 * @since 12/30/20
 */

@Service
@Primary
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final TelegramNotificationService telegramService;

    @Override
    public void send(String message) {
        telegramService.send(message);
    }
}
