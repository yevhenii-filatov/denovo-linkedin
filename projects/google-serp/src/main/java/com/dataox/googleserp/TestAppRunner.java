package com.dataox.googleserp;

import com.dataoxx.scrapingutils.captcha.CaptchaSolver;
import com.dataoxx.scrapingutils.configuration.properties.NotificationProperties;
import com.dataoxx.scrapingutils.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * @author Yevhenii Filatov
 * @since 1/15/21
 */

@Service
@RequiredArgsConstructor
public class TestAppRunner implements ApplicationRunner {
    private final NotificationProperties notificationProperties;
    private final NotificationService notificationService;
    private final CaptchaSolver captchaSolver;
    /*private final CaptchaProperties captchaProperties;
    private final ProxyProperties proxyProperties;
    private final SearchProperties searchProperties;
    private final NotificationProperties notificationProperties;*/

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println();
    }
}
