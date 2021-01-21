package com.dataox.captchasolver.dto;

import lombok.Data;

/**
 * @author Yevhenii Filatov
 * @since 12/2/20
 */

@Data
public class CreateCaptchaTaskRequest {
    private String clientKey;
    private Task task;

    @Data
    public static class Task {
        private String type;
        private String websiteURL;
        private String websiteKey;
        private String websitePublicKey;
    }
}
