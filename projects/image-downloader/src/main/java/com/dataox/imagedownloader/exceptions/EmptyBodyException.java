package com.dataox.imagedownloader.exceptions;

import java.io.IOException;

public class EmptyBodyException extends IOException {

    public EmptyBodyException(String message) {
        super(message);
    }
}
