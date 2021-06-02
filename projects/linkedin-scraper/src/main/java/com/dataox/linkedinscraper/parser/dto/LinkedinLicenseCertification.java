package com.dataox.linkedinscraper.parser.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"updatedAt", "itemSource"})
@ToString(exclude = {"updatedAt", "itemSource"})
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

//    @Max(30)
    private String expirationDate;

//    @Max(50)
    private String credentialId;
}
