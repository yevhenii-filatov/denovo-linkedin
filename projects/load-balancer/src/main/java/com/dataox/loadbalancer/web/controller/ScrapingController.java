package com.dataox.loadbalancer.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@RestController
public class ScrapingController {

    @GetMapping("/hi")
    public String hello() {
        return "Hello";
    }
}
