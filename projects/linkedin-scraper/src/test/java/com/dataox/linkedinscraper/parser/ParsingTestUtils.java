package com.dataox.linkedinscraper.parser;

import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

public class ParsingTestUtils {

    private ParsingTestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String loadResource(String resPath) throws IOException {
        return IOUtils.resourceToString(resPath, StandardCharsets.UTF_8);
    }

    public static Comparator<String> hashedStingComparator() {
        return (a, b) -> {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null || b == null) {
                return 1;
            }
            String aHash = a.startsWith("md5:") ? a.substring(4) : getMd5Hex(a);
            String bHash = b.startsWith("md5:") ? b.substring(4) : getMd5Hex(b);
            return aHash.compareTo(bHash);
        };
    }

    public static String getMd5Hex(String text) {
        if (text == null) {
            text = "";
        }

        return DigestUtils.md5DigestAsHex(text.getBytes());
    }


}
