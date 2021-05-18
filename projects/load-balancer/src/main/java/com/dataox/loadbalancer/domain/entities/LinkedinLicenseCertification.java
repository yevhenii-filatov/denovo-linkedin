package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_license_certification")
@NoArgsConstructor
public class LinkedinLicenseCertification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotBlank
    @Column(name = "item_source", columnDefinition = "TEXT")
    private String itemSource;

    @NotBlank
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @NotBlank
    @Column(name = "issuer", columnDefinition = "TEXT")
    private String issuer;

    @Column(name = "issuer_profile_url", columnDefinition = "TEXT")
    private String issuerProfileUrl;

    @Column(name = "issued_date", columnDefinition = "TEXT")
    private String issuedDate;

    @Column(name = "expiration_date", columnDefinition = "TEXT")
    private String expirationDate;

    @Column(name = "credential_id", columnDefinition = "TEXT")
    private String credentialId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
