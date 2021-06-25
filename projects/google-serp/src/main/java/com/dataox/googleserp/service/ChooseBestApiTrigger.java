package com.dataox.googleserp.service;

import com.dataox.googleserp.configuration.properties.ServiceProperties;
import com.dataox.okhttputils.OkHttpTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 20/04/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChooseBestApiTrigger {
    private final ObjectMapper objectMapper;
    private final OkHttpTemplate okHttpTemplate;
    private final ServiceProperties serviceProperties;

    public void triggerAPI(List<Long> denovoIds) {
        triggerChooseBestEndpoint(denovoIds);
    }

    private void triggerChooseBestEndpoint(List<Long> denovoIds) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        denovoIds.forEach(arrayNode::add);
        Request request = getRequest(arrayNode);
        try {
            okHttpTemplate.request(request);
        } catch (IOException e) {
            log.error("Failed to trigger choose best endpoint with denovo ids: {}", denovoIds, e);
        }
    }

    private Request getRequest(ArrayNode arrayNode) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, arrayNode.toString());
        return new Request.Builder()
                .url(serviceProperties.getChooseBestEndpointUrl())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
    }
}
