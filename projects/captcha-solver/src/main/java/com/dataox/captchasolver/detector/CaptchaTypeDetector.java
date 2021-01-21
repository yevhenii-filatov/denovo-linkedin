package com.dataox.captchasolver.detector;

import org.jsoup.nodes.Document;

/**
 * @author Yevhenii Filatov
 * @since 8/6/20
 */

public interface CaptchaTypeDetector {
    CaptchaType detect(Document pageDocument);
}
