package com.dataox.captchasolver.detector;


import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;


import java.util.Objects;

/**
 * @author Yevhenii Filatov
 * @since 7/20/20
 */

@Service
public class LinkedinCaptchaTypeDetector implements CaptchaTypeDetector {
    private static final String FUNCAPTCHA_IFRAME_SELECTOR = "iframe[src*=funCaptchaInternal]";
    private static final String RECAPTCHA_V2_IFRAME_SELECTOR = "iframe[src*=captchaInternal]";

    public CaptchaType detect(Document pageDocument) {
        if (funcaptchaPresent(pageDocument)) {
            return CaptchaType.FUNCAPTCHA;
        }
        if (recaptchaV2Present(pageDocument)) {
            return CaptchaType.RECAPTCHA_V2;
        }
        return CaptchaType.NONE;
    }

    private boolean funcaptchaPresent(Document pageDocument) {
        return Objects.nonNull(pageDocument.selectFirst(FUNCAPTCHA_IFRAME_SELECTOR));
    }

    private boolean recaptchaV2Present(Document pageDocument) {
        return Objects.nonNull(pageDocument.selectFirst(RECAPTCHA_V2_IFRAME_SELECTOR));
    }
}
