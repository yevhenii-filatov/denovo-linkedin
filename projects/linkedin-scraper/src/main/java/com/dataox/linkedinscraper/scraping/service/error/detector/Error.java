package com.dataox.linkedinscraper.scraping.service.error.detector;

public interface Error {
    String getCode();
    String getMessage();
    boolean isCritical();
}