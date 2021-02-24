package com.dataox.loadbalancer.domain.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "linkedin_profile")
@NoArgsConstructor
public class LinkedinProfile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "search_result_id", referencedColumnName = "id")
    private SearchResult searchResult;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotBlank
    @Column(name = "profile_url")
    private String profileUrl;

    @OneToOne(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LinkedinBasicProfileInfo linkedinBasicProfileInfo;

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinExperience> linkedinExperiences = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinEducation> linkedinEducations = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinRecommendation> linkedinRecommendations = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinLicenseCertification> linkedinLicenseCertifications = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinVolunteerExperience> linkedinVolunteerExperiences = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinInterest> linkedinInterests = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinSkill> linkedinSkills = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinActivity> linkedinActivities = new ArrayList<>();

    @OneToMany(mappedBy = "linkedinProfile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinAccomplishment> linkedinAccomplishments = new ArrayList<>();
}
