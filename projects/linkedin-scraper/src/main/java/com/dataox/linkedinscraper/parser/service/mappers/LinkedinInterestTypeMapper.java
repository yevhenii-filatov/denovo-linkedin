package com.dataox.linkedinscraper.parser.service.mappers;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinInterestType;
import com.dataox.linkedinscraper.parser.exceptions.LinkedinTypeMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class LinkedinInterestTypeMapper {

    public LinkedinInterestType map(String type) {
        log.debug("Type to map: {}", type);
        return Arrays.stream(LinkedinInterestType.values())
                .filter(linkedinType -> linkedinType.getType().equals(type))
                .findFirst().orElseThrow(() -> new LinkedinTypeMappingException("failed map LinkedinInterestType: " + type));
    }
}