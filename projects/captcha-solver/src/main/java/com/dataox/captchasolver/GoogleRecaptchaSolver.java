package com.dataox.captchasolver;

import com.dataox.captchasolver.configuration.CaptchaProperties;
import com.dataox.captchasolver.dto.CreateCaptchaTaskRequest;
import com.dataox.captchasolver.dto.CreateCaptchaTaskResponse;
import com.dataox.captchasolver.dto.GetCaptchaTaskResultRequest;
import com.dataox.captchasolver.dto.GetCaptchaTaskResultResponse;
import com.dataox.captchasolver.exeptions.CaptchaSolvingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.dataox.CommonUtils.sleepFor;
import static com.dataox.WebDriverUtils.*;
import static com.google.common.base.Ascii.equalsIgnoreCase;

/**
 * @author Yevhenii Filatov
 * @since 12/2/20
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleRecaptchaSolver implements CaptchaSolver {
    private static final String CAPTCHA_TYPE = "NoCaptchaTaskProxyless";
    private static final String CREATE_TASK_API_URL = "https://api.anti-captcha.com/createTask";
    private static final String GET_TASK_RESULT_API_URL = "https://api.anti-captcha.com/getTaskResult";
    private static final String G_RECAPTCHA_RESPONSE_TEXTAREA_SELECTOR = "#g-recaptcha-response";
    private static final By CAPTCHA_WRAPPER_SELECTOR = By.className("g-recaptcha");
    private static final Long ELEMENT_SEARCH_TIMEOUT = 15L;
    private final RestTemplate restTemplate;
    private final CaptchaProperties captchaProperties;

    @Override
    public void solve(WebDriver webDriver) {
        String websiteUrl = webDriver.getCurrentUrl();
        String dataSitekey = retrieveDataSitekey(webDriver);
        CreateCaptchaTaskResponse createTaskResponse = Objects.requireNonNull(createCaptchaTask(websiteUrl, dataSitekey));
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
        String hash = taskResult.getSolution().getGRecaptchaResponse();
        enterHash(webDriver, hash);
    }

    private void enterHash(WebDriver webDriver, String hash) {
        showHiddenElement(webDriver, G_RECAPTCHA_RESPONSE_TEXTAREA_SELECTOR);
        WebElement recaptchaTokenArea = findClickableBy(webDriver, By.cssSelector(G_RECAPTCHA_RESPONSE_TEXTAREA_SELECTOR), ELEMENT_SEARCH_TIMEOUT)
                .orElseThrow(() -> CaptchaSolvingException.notFound("Textarea for entering answer hash"));
        recaptchaTokenArea.sendKeys(hash);
        hideElement(webDriver, G_RECAPTCHA_RESPONSE_TEXTAREA_SELECTOR);
    }

    private boolean responseHasErrors(GetCaptchaTaskResultResponse taskResult) {
        return taskResult.getErrorId() != 0;
    }

    private boolean taskIsInProgress(GetCaptchaTaskResultResponse taskResult) {
        return equalsIgnoreCase(taskResult.getStatus(), "processing");
    }

    private GetCaptchaTaskResultResponse getTaskResult(Long taskId) {
        GetCaptchaTaskResultRequest resultRequest = new GetCaptchaTaskResultRequest();
        resultRequest.setClientKey(captchaProperties.getAntiCaptcha().getToken());
        resultRequest.setTaskId(taskId);
        return restTemplate.postForObject(GET_TASK_RESULT_API_URL, resultRequest, GetCaptchaTaskResultResponse.class);
    }

    private CreateCaptchaTaskResponse createCaptchaTask(String websiteUrl, String dataSitekey) {
        CreateCaptchaTaskRequest.Task task = new CreateCaptchaTaskRequest.Task();
        task.setType(CAPTCHA_TYPE);
        task.setWebsiteURL(websiteUrl);
        task.setWebsiteKey(dataSitekey);
        CreateCaptchaTaskRequest createTaskRequest = new CreateCaptchaTaskRequest();
        createTaskRequest.setClientKey(captchaProperties.getAntiCaptcha().getToken());
        createTaskRequest.setTask(task);
        return restTemplate.postForObject(CREATE_TASK_API_URL, createTaskRequest, CreateCaptchaTaskResponse.class);
    }

    private String retrieveDataSitekey(WebDriver webDriver) {
        WebElement captchaWrapper = findClickableBy(webDriver, CAPTCHA_WRAPPER_SELECTOR, ELEMENT_SEARCH_TIMEOUT)
                .orElseThrow(() -> CaptchaSolvingException.notFound("Element with 'data-sitekey' attribute"));
        return captchaWrapper.getAttribute("data-sitekey");
    }
}
