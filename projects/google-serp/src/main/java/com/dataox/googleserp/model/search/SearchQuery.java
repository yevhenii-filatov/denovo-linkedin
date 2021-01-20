package com.dataox.googleserp.model.search;

import lombok.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchQuery {

    @NonNull
    private final List<String> parameters;

    public static SearchQuery fromQueryParameters(String... queryParameters) {
        return new SearchQuery(Arrays.asList(queryParameters));
    }

    public String asString() {
        return String.join("+", encodeParameters());
    }

    @SneakyThrows
    private List<String> encodeParameters() {
        List<String> encodedParameters = new ArrayList<>();
        for (String parameter : parameters) {
            encodedParameters.add(URLEncoder.encode(parameter, StandardCharsets.UTF_8.name()));
        }
        return encodedParameters;
    }
}
