package com.dataox.loadbalancer.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class InitialDataDTO {
    @NotNull
    Long denovoId;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    String firmName;
}
