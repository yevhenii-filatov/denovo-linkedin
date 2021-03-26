package com.dataox.loadbalancer.domain.converter;

import com.dataox.linkedinscraper.dto.OptionalFieldsContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;

/**
 * @author Dmitriy Lysko
 * @since 26/03/2021
 */
public class OptionalFieldsConverter implements AttributeConverter<OptionalFieldsContainer, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(OptionalFieldsContainer attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public OptionalFieldsContainer convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, OptionalFieldsContainer.class);
    }
}
