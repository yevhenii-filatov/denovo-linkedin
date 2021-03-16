package com.dataox.linkedinscraper.service.error.detector;

public interface Error {
    String getCode();
    String getMessage();
    boolean isCritical();
}