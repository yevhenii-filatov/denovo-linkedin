package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_endorsement")
@NoArgsConstructor
public class LinkedinEndorsement {

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
    @Column(name = "endorser_full_name", columnDefinition = "TEXT")
    private String endorserFullName;

    @Column(name = "endorser_headline", columnDefinition = "TEXT")
    private String endorserHeadline;

    @NotBlank
    @Column(name = "endorser_connection_degree", columnDefinition = "TEXT")
    private String endorserConnectionDegree;

    @NotBlank
    @Column(name = "endorser_profile_url", columnDefinition = "TEXT")
    private String endorserProfileUrl;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    private LinkedinSkill linkedinSkill;
}
