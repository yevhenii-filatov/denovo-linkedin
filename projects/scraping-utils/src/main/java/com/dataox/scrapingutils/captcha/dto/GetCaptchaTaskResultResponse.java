package com.dataox.scrapingutils.captcha.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

/**
 * @author Yevhenii Filatov
 * @since 12/2/20
 */

@Data
public class GetCaptchaTaskResultResponse {
    private Integer errorId;
    private String status;
    private Solution solution;
    private String cost;
    private String ip;
    private Instant createTime;
    private Instant endTime;
    private Integer solveCount;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Solution {
        @JsonProperty("gRecaptchaResponse")
        private String gRecaptchaResponse;
    }
}
