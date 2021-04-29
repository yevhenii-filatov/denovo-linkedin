package com.dataox.loadbalancer.dto;

import com.dataox.linkedinscraper.dto.OptionalFieldsContainer;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Dmitriy Lysko
 * @since 25/03/2021
 */
@Data
@AllArgsConstructor
public class LinkedinProfileToUpdateDTO {
    @NotNull
    Long linkedinProfileId;
    @NotNull
    OptionalFieldsContainer optionalFieldsContainer;
}
