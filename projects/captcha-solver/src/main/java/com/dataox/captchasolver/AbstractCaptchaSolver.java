package com.dataox.captchasolver;

import com.dataox.captchasolver.configuration.CaptchaProperties;
import com.dataox.captchasolver.dto.CreateCaptchaTaskResponse;
import com.dataox.captchasolver.dto.GetCaptchaTaskResultRequest;
import com.dataox.captchasolver.dto.GetCaptchaTaskResultResponse;
import com.dataox.captchasolver.exeptions.CaptchaSolvingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.dataox.CommonUtils.sleepFor;
import static com.google.common.base.Ascii.equalsIgnoreCase;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCaptchaSolver implements CaptchaSolver {
    protected static final String CREATE_TASK_API_URL = "https://api.anti-captcha.com/createTask";
    private static final String GET_TASK_RESULT_API_URL = "https://api.anti-captcha.com/getTaskResult";
    private final CaptchaProperties captchaProperties;
    private final RestTemplate restTemplate;

    private boolean responseHasErrors(GetCaptchaTaskResultResponse taskResult) {
        return taskResult.getErrorId() != 0;
    }

    private boolean taskIsInProgress(GetCaptchaTaskResultResponse taskResult) {
        return equalsIgnoreCase(taskResult.getStatus(), "processing");
    }

    protected GetCaptchaTaskResultResponse getTaskResult(Long taskId) {
        GetCaptchaTaskResultRequest resultRequest = new GetCaptchaTaskResultRequest();
        resultRequest.setClientKey(captchaProperties.getAntiCaptcha().getToken());
        resultRequest.setTaskId(taskId);
        return restTemplate.postForObject(GET_TASK_RESULT_API_URL, resultRequest, GetCaptchaTaskResultResponse.class);
    }

    protected GetCaptchaTaskResultResponse retrieveGetCaptchaTaskResultResponse(WebDriver webDriver) {
        String websiteUrl = webDriver.getCurrentUrl();
        String captchaSiteKey = retrieveCaptchaSiteKey(webDriver);

        CreateCaptchaTaskResponse createTaskResponse = Objects.requireNonNull(createCaptchaTask(websiteUrl, captchaSiteKey));
        Long taskId = createTaskResponse.getTaskId();
        log.info("Recaptcha solving task #{} was successfully created.", taskId);
        GetCaptchaTaskResultResponse taskResult;
        do {
            taskResult = Objects.requireNonNull(getTaskResult(createTaskResponse.getTaskId()));
            log.info("Recaptcha solving task #{} status: {}", taskId, taskResult.getStatus());
            sleepFor(5);
        } while (taskIsInProgress(taskResult));

        if (responseHasErrors(taskResult)) {
            throw new CaptchaSolvingException(String.format("Captcha solving failed, error code - %d%n", taskResult.getErrorId()));
        }
        return taskResult;
    }

    protected abstract String retrieveCaptchaSiteKey(WebDriver webDriver);

    protected abstract CreateCaptchaTaskResponse createCaptchaTask(String websiteUrl, String captchaSiteKey);
}
