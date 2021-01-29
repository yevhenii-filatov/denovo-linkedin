package com.dataox.loadbalancer.domain.entities;

import com.dataox.loadbalancer.domain.types.LinkedinRecommendationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_recommendation")
@NoArgsConstructor
public class LinkedinRecommendation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkedinRecommendationType linkedinRecommendationType;

    @NotBlank
    @Column(name = "person_full_name")
    private String personFullName;

    @NotBlank
    @Column(name = "person_profile_url")
    private String personProfileUrl;

    @NotBlank
    @Column(name = "person_headline")
    private String personHeadline;

    @NotBlank
    @Column(name = "person_extra_info")
    private String personExtraInfo;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
