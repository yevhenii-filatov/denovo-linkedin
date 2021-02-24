package com.dataox.linkedinscraper.parser.utils;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

@Service
public class TimeConverter {

    public Instant getAbsoluteTime(String when) {
        return ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC)
                .minus(toLong(when), getChronoUnit(when)).toInstant();
    }

    private ChronoUnit getChronoUnit(String when) {
        return Arrays.stream(ChronoUnit.values())
                .filter(chronoUnit -> {
                    String date = extractLetters(when);
                    return chronoUnit.toString().toLowerCase().startsWith(handleYear(date));
                })
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    private String handleYear(String date){
        return date.equals("yr")
                ? "y"
                : date;
    }

    private long toLong(String when) {
        return Long.parseLong(extractNumbers(when));
    }

    private String extractNumbers(String when) {
        return normalizeSpace(when.replaceAll("\\D+", ""));
    }

    private String extractLetters(String when) {
        return normalizeSpace(when.replace(extractNumbers(when), ""));
    }
}
