package com.dataox.imagedownloader.api.controllers;

import com.dataox.imagedownloader.api.dto.ImageCredentials;
import com.dataox.imagedownloader.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImagesController {
    private final ImageService imageService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveImage(@RequestBody ImageCredentials imageCredentials) throws IOException {
        imageService.saveImage(imageCredentials);
    }

    @GetMapping(value = "/show/one/{imageName}", produces = {MediaType.IMAGE_PNG_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public byte[] showOneImage(@PathVariable("imageName") String imageName) throws IOException {
        return imageService.getOneImageAsBytes(imageName);
    }

    @GetMapping(value = "/get/one/{imageName}")
    public HttpEntity<byte[]> getOneImage(@PathVariable String imageName) throws IOException {
        return imageService.getOneImage(imageName);
    }

    @GetMapping("/get/all")
    public HttpEntity<byte[]> getAllImagesAsZip() throws IOException {
        return imageService.getAllImageAsZip();
    }

}
