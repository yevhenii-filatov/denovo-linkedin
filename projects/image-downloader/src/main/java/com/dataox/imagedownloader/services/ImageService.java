package com.dataox.imagedownloader.services;

import com.dataox.imagedownloader.api.dto.ImageCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageSaver imageSaver;
    private final ImageReader imageReader;

    public void saveImage(ImageCredentials imageCredentials) throws IOException {
        imageSaver.saveImage(imageCredentials);
    }

    public byte[] getOneImageAsBytes(String imageName) throws IOException {
        return imageReader.getOneImage(imageName);
    }

    public HttpEntity<byte[]> getOneImage(String imageName) throws IOException {
        byte[] imageBytes = getOneImageAsBytes(imageName);
        return convertToHttpEntity(imageName, imageBytes, MediaType.IMAGE_PNG);
    }

    public HttpEntity<byte[]> getAllImageAsZip() throws IOException {
        byte[] allImagesAsZip = imageReader.getAllImagesAsZip();
        return convertToHttpEntity("AllImages.zip", allImagesAsZip, MediaType.MULTIPART_FORM_DATA);
    }

    private HttpEntity<byte[]> convertToHttpEntity(String fileName, byte[] fileInBytes, MediaType mediaType) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(mediaType);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName);
        header.setContentLength(fileInBytes.length);
        return new HttpEntity<>(fileInBytes, header);
    }
}
