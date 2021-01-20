package com.dataox.imagedownloader.api.controllers;

import com.dataox.imagedownloader.api.dto.ImageCredentials;
import com.dataox.imagedownloader.services.ImageReader;
import com.dataox.imagedownloader.services.ImageSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImagesController {
    private final ImageSaver imageSaver;
    private final ImageReader imageReader;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveImage(@RequestBody ImageCredentials imageCredentials) throws IOException {
        imageSaver.saveImage(imageCredentials);
    }

    @GetMapping(value = "/show/one",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] showOneImage(@RequestParam String imageName) throws IOException {
        return imageReader.getOneImage(imageName);
    }

    @GetMapping(value = "/get/one")
    public HttpEntity<byte[]> getOneImage(@RequestParam String imageName) throws IOException {
        byte[] imageBytes = imageReader.getOneImage(imageName);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + imageName);
        header.setContentLength(imageBytes.length);
        return new HttpEntity<>(imageBytes, header);
    }

    @GetMapping("/get/all")
    public HttpEntity<byte[]> getAllImagesAsZip() throws IOException {
        byte[] allImagesAsZip = imageReader.getAllImagesAsZip();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.MULTIPART_FORM_DATA);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + "AllImages.zip");
        header.setContentLength(allImagesAsZip.length);
        return new HttpEntity<>(allImagesAsZip, header);
    }
}
