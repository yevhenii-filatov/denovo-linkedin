package com.dataox.googleserp.service.operiaNotifications;

import com.dataox.notificationservice.configuration.NotificationsProperties;
import com.dataox.notificationservice.service.NotificationsService;
import com.dataox.okhttputils.OkHttpTemplate;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Viacheslav Yakovenko
 * @since 26/05/2021
 */
@Slf4j
@Data
@Service
@EnableScheduling
public class OperiaNotifications {

    private final OkHttpTemplate okHttpTemplate;
    private final NotificationsService notificationsService;
    private final NotificationsProperties notificationsProperties;

    private Long amountOfOperiaQueriesLeft = 0L;

    @Scheduled(cron = "0 0 10 * * *")
    public void check() {

        Request request = new Request.Builder()
                .url("https://run.operia.io/9ea06d8cd1351366/credits/")
                .get()
                .addHeader("Content-Type", "application/json")
                .build();

        String credit = "";
        try {
            credit = okHttpTemplate.request(request);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("GoogleSearch: Operia notification message was not send, check logs for details.");
            notificationsService.sendInternal("GoogleSearch: Operia notification message was not send, check logs for details.");
        }

        double creditLong = Double.parseDouble(credit);
        long amountOfOperiaQueriesLeftCheck = (long) (creditLong / 0.00015);

        if (amountOfOperiaQueriesLeft != amountOfOperiaQueriesLeftCheck) {

            amountOfOperiaQueriesLeft = amountOfOperiaQueriesLeftCheck;
            Map<String, String> formBody = new HashMap<>();
            formBody.put("token", notificationsProperties.getSlack().getSlackToken());
            formBody.put("channel", "operia_notififications");
            String text = String.format("Operia has %d querries left.", amountOfOperiaQueriesLeft);
            formBody.put("text", text);

            try {
                okHttpTemplate.post("https://slack.com/api/chat.postMessage", formBody);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("Failed to send message to operia slack channel.");
            }

        }

    }
}
