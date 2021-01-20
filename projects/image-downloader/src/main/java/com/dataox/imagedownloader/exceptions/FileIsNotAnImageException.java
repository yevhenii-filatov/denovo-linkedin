package com.dataox.imagedownloader.exceptions;

import java.io.IOException;

public class FileIsNotAnImageException extends IOException {

    public FileIsNotAnImageException(String message) {
        super(message);
    }
}
