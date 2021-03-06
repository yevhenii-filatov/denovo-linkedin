package com.dataox.imagedownloader.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.commons.lang3.StringUtils.appendIfMissing;

@Service
@Slf4j
public class ImageReader {

    @Value("${images.directory.path}")
    private String imagesDirectoryPath;

    public byte[] getOneImage(String imageName) {
        imageName = appendIfMissing(imageName, ".png");
        File file = new File(imagesDirectoryPath.concat(imageName));
        checkIfFileExist(file);
        log.info("Found one image: {}", imageName);
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void checkIfFileExist(File file) {
        if (!file.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested image '" + file.getName() + "' is not present");
        }
    }

    public byte[] getAllImagesAsZip()  {
        log.info("Started creating zip with all images");
        File dir = new File(imagesDirectoryPath);
        File[] imagesArray = dir.listFiles();
        if (imagesArray == null || imagesArray.length == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Images directory is empty or not present");
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bo);
        buildZIP(imagesArray, zipOut);
        log.info("Finished creating zip with all images");
        return bo.toByteArray();
    }

    private void buildZIP(File[] imagesArray, ZipOutputStream zipOut) {
        try {
            for (File image : imagesArray) {
                if (!image.isFile()) continue;
                ZipEntry zipEntry = new ZipEntry(image.getName());
                zipOut.putNextEntry(zipEntry);
                zipOut.write(IOUtils.toByteArray(new FileInputStream(image)));
                zipOut.closeEntry();
            }
            zipOut.close();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
