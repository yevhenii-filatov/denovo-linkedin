package com.dataox.imagedownloader.exceptions;

public class FileIsNotAnImageException extends RuntimeException {

    public FileIsNotAnImageException(String message) {
        super(message);
    }
}
