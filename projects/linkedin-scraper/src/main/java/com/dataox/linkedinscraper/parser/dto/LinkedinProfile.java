package com.dataox.linkedinscraper.parser.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkedinProfile {


    private SearchResult searchResult;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String profileUrl;

    @Valid
    private LinkedinBasicProfileInfo linkedinBasicProfileInfo;

    @Valid
    private List<LinkedinExperience> linkedinExperiences = new ArrayList<>();

    @Valid
    private List<LinkedinEducation> linkedinEducations = new ArrayList<>();

    @Valid
    private List<LinkedinRecommendation> linkedinRecommendations = new ArrayList<>();

    @Valid
    private List<LinkedinLicenseCertification> linkedinLicenseCertifications = new ArrayList<>();

    @Valid
    private List<LinkedinVolunteerExperience> linkedinVolunteerExperiences = new ArrayList<>();

    @Valid
    private List<LinkedinInterest> linkedinInterests = new ArrayList<>();

    @Valid
    private List<LinkedinSkill> linkedinSkills = new ArrayList<>();

    @Valid
    private List<LinkedinActivity> linkedinActivities = new ArrayList<>();

    @Valid
    private List<LinkedinAccomplishment> linkedinAccomplishments = new ArrayList<>();
}
