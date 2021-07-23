package com.dataox.googleserp.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Dmitriy Lysko
 * @since 19/04/2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitialDataDTO {
    @NotNull
    Long denovoId;
    @NotNull
    String firstName;
    @NotNull
    String middleName;
    @NotNull
    String lastName;
    @NotNull
    String firmName;
    String linkedinUrl;
    @NotNull
    @Max(value = 7)
    @Min(value = 1)
    Integer searchStep;
}
