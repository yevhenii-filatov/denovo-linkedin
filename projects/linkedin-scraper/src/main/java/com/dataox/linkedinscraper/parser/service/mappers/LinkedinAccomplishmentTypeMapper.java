package com.dataox.linkedinscraper.parser.service.mappers;

import com.dataox.linkedinscraper.parser.dto.types.LinkedinAccomplishmentType;
import com.dataox.linkedinscraper.parser.exceptions.LinkedinTypeMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class LinkedinAccomplishmentTypeMapper {

    public LinkedinAccomplishmentType map(String type) {
        log.debug("Type to map: {}", type);
        return Arrays.stream(LinkedinAccomplishmentType.values())
                .filter(linkedinType -> linkedinType.getType().equals(type))
                .findFirst().orElseThrow(() -> new LinkedinTypeMappingException("failed map LinkedinAccomplishmentType: " + type));
    }
}
