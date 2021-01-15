package com.dataox.scrapingutils.captcha.dto;

import lombok.Data;

/**
 * @author Yevhenii Filatov
 * @since 12/2/20
 */

@Data
public class CreateCaptchaTaskResponse {
    private Long errorId;
    private Long taskId;
}
