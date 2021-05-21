package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
public class LinkedinLicenseCertification {

//    @NotNull
    private Instant updatedAt;

//    @NotBlank
    private String itemSource;

//    @NotBlank
    private String name;

//    @NotBlank
    private String issuer;

    private String issuerProfileUrl;

//    @Max(20)
    private String issuedDate;

    @Max(30)
    private String expirationDate;

    @Max(50)
    private String credentialId;
}
