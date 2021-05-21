package com.example.demo;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @author Viacheslav_Yakovenko
 * @since 14.05.2021
 */
@Controller
public class ApplicationController {

    @PostMapping("/choose")
    public ResponseEntity choose(@RequestBody String json) {
        System.out.println("PRINTING A FUCKING JSON!!!");
        System.out.println(json);

        Gson gson = new Gson();
        List<Double> denovoId = gson.fromJson(json, (Type) List.class);
        ScrapingDTO scrapingDTO = new ScrapingDTO(denovoId.get(0), 1);


        String scrapingDTOJson = gson.toJson(Collections.singletonList(scrapingDTO));
        okhttp3.RequestBody.create(MediaType.parse("application/json"), scrapingDTOJson);

        Request request = new Request.Builder()
                .url("http://localhost:8080/api/v1/scraping/initial")
                .method("POST", okhttp3.RequestBody.create(MediaType.parse("application/json"), scrapingDTOJson))
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            System.out.println(
                new OkHttpClient().newCall(request).execute()
                                                            );
        } catch (IOException e) {
            System.out.println("Failed to trigger choose best endpoint with denovo ids: {}");
        }


        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    public class ScrapingDTO {
        Double denovoId;
        int searchPosition;

    }
//Request.Builder()
//        .url(serviceProperties.getChooseBestEndpointUrl())
//            .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .build();

}
