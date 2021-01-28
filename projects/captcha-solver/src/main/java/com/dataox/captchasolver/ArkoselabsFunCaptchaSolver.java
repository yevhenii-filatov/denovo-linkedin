package com.dataox.captchasolver;

import com.dataox.captchasolver.configuration.CaptchaProperties;
import com.dataox.captchasolver.dto.CreateCaptchaTaskRequest;
import com.dataox.captchasolver.dto.CreateCaptchaTaskResponse;
import com.dataox.captchasolver.dto.GetCaptchaTaskResultResponse;
import com.dataox.captchasolver.exeptions.CaptchaSolvingException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.dataox.WebDriverUtils.findElementBy;

@Slf4j
@Service
public class ArkoselabsFunCaptchaSolver extends AbstractCaptchaSolver {
    private static final String CAPTCHA_TYPE = "FunCaptchaTaskProxyless";
    private static final By CAPTCHA_KEY_SELECTOR = By.cssSelector("form#captcha-challenge input[name*=captchaSiteKey]");
    private final RestTemplate restTemplate;
    private final CaptchaProperties captchaProperties;

    public ArkoselabsFunCaptchaSolver(CaptchaProperties captchaProperties, RestTemplate restTemplate) {
        super(captchaProperties, restTemplate);
        this.captchaProperties = captchaProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public void solve(WebDriver webDriver) {
        GetCaptchaTaskResultResponse taskResult = retrieveGetCaptchaTaskResultResponse(webDriver);
        String token = taskResult.getSolution().getToken();
        enterCaptchaToken(webDriver, token);
    }

    private void enterCaptchaToken(WebDriver webDriver, String token) {
        String scriptTemplate = "document.querySelector('form#captcha-challenge input[name=captchaUserResponseToken]').value='%s'";
        ((JavascriptExecutor) webDriver).executeScript(String.format(scriptTemplate, token));
    }

    @Override
    protected CreateCaptchaTaskResponse createCaptchaTask(String websiteUrl, String captchaSiteKey) {
        CreateCaptchaTaskRequest.Task task = new CreateCaptchaTaskRequest.Task();
        task.setType(CAPTCHA_TYPE);
        task.setWebsiteURL(websiteUrl);
        task.setWebsitePublicKey(captchaSiteKey);
        CreateCaptchaTaskRequest createTaskRequest = new CreateCaptchaTaskRequest();
        createTaskRequest.setClientKey(captchaProperties.getAntiCaptcha().getToken());
        createTaskRequest.setTask(task);
        return restTemplate.postForObject(CREATE_TASK_API_URL, createTaskRequest, CreateCaptchaTaskResponse.class);
    }

    @Override
    protected String retrieveCaptchaSiteKey(WebDriver webDriver) {
        WebElement captchaKey = findElementBy(webDriver, CAPTCHA_KEY_SELECTOR);
        if (Objects.isNull(captchaKey)) {
            throw CaptchaSolvingException.notFound("Element with 'captchaSiteKey' attribute");
        }
        return captchaKey.getAttribute("value");
    }
}
