package com.dataox.linkedinscraper.parser.service.mappers;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinRecommendationType;
import com.dataox.linkedinscraper.parser.exceptions.LinkedinTypeMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class LinkedinRecommendationTypeMapper {

    public LinkedinRecommendationType map(String type) {
        log.debug("Type to map: {}", type);
        return Arrays.stream(LinkedinRecommendationType.values())
                .filter(linkedinType -> linkedinType.getType().equals(type))
                .findFirst().orElseThrow(() -> new LinkedinTypeMappingException("failed map LinkedinRecommendationType: " + type));
    }
}