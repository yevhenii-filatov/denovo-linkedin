package com.dataox.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class NotificationsServiceImpl implements NotificationsService {
    private final TelegramNotificationsServiceProvider telegramNotificationsServiceProvider;
    private final SlackNotificationsServiceProvider slackNotificationsServiceProvider;

    @Override
    public void sendInternal(String message) {
        telegramNotificationsServiceProvider.send(message);
    }

    @Override
    public void sendExternal(String message) {
        slackNotificationsServiceProvider.send(message);
    }

    @Override
    public void sendAll(String message) {
        telegramNotificationsServiceProvider.send(message);
        slackNotificationsServiceProvider.send(message);
    }
}

