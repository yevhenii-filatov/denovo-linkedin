package com.dataox.linkedinscraper.parser.utils;

import com.dataox.linkedinscraper.parser.exceptions.TimeConvertingException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dataox.linkedinscraper.parser.utils.ParsingUtils.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class TimeConverter {

    private static final Pattern YEAR_PATTERN = Pattern.compile("\\d{4}");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy", Locale.US);

    public Instant getAbsoluteTime(String when) {
        String numbers = extractNumbers(when);
        return ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC)
                .minus(toLong(numbers), getChronoUnit(when)).toInstant();
    }

    private ChronoUnit getChronoUnit(String when) {
        return Arrays.stream(ChronoUnit.values())
                .filter(chronoUnit -> chronoUnit.toString().toLowerCase().startsWith(extractLetters(when)))
                .findFirst()
                .orElseThrow(() -> new TimeConvertingException("Failed to get the corresponding ChronoUnit"));
    }

    public LocalDate toLocalDate(String date) {
        if (isBlank(date)) {
            return null;
        }
        String capitalizedDate = capitalizeFirstLetter(date);
        return parseExactDate(capitalizedDate);

    }

    private static LocalDate parseExactDate(String date) {
        LocalDate localDate = null;
        if (isNotBlank(date)) {
            try {
                localDate = LocalDate.parse(date, TimeConverter.DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                try {
                    localDate = YearMonth.parse(date, TimeConverter.DATE_FORMATTER).atDay(1);
                } catch (DateTimeParseException e1) {
                    Matcher matcher = YEAR_PATTERN.matcher(date);
                    if (matcher.find()) {
                        localDate = LocalDate.of(Integer.parseInt(matcher.group()), 1, 1);
                    }
                }
            }
        }
        return localDate;
    }
}
