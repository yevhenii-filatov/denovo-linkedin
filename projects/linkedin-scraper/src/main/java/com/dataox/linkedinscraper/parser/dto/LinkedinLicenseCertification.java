package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinLicenseCertification {

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String itemSource;

    @NotBlank
    private String name;

    @NotBlank
    private String issuer;

    private String issuerProfileUrl;

    private String issuedDate;

    private String expirationDate;

    private String credentialId;
}
