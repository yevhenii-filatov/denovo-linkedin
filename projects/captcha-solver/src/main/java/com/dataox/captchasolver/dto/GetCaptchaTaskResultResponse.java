package com.dataox.captchasolver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
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
    private Timestamp createTime;
    private Timestamp endTime;
    private Integer solveCount;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Solution {
        @JsonProperty("gRecaptchaResponse")
        private String gRecaptchaResponse;

        @JsonProperty("token")
        private String token;
    }
}
