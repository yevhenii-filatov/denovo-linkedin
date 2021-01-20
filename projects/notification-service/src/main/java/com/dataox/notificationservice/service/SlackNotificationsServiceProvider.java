package com.dataox.notificationservice.service;

import com.dataox.notificationservice.configuration.NotificationsProperties;
import com.dataox.okhttputils.OkHttpTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service("Slack-notifications")
public class SlackNotificationsServiceProvider implements NotificationsServiceProvider {
    private static final String CHAT_POST_MESSAGE_API_URL = "https://slack.com/api/chat.postMessage";
    private final NotificationsProperties notificationsProperties;
    private final OkHttpTemplate okHttpTemplate;

    @Override
    public void send(String message) {
        try {
            okHttpTemplate.post(CHAT_POST_MESSAGE_API_URL, prepareFormBody(message));
        } catch (IOException e) {
            log.error("Failed to send slack message: {}", message);
            log.error("Due to: {} {}", e.getMessage(), e.getClass().getName());
        }
    }

    private Map<String, String> prepareFormBody(String messageText) {
        String token = notificationsProperties.getSlack().getSlackToken();
        String channel = notificationsProperties.getSlack().getNotificationsChannel();
        Map<String, String> formBody = new HashMap<>();
        formBody.put("token", token);
        formBody.put("channel", channel);
        formBody.put("text", messageText);
        return formBody;
    }
}
