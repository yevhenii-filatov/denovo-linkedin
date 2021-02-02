package com.dataox.linkedinscraper.parser.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LinkedinProfile {

    @NotNull
    private SearchResult searchResult;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @NotBlank
    private String profileUrl;

    private LinkedinBasicProfileInfo linkedinBasicProfileInfo;

    private List<LinkedinExperience> linkedinExperiences = new ArrayList<>();

    private List<LinkedinEducation> linkedinEducations = new ArrayList<>();

    private List<LinkedinRecommendation> linkedinRecommendations = new ArrayList<>();

    private List<LinkedinLicenseCertification> linkedinLicenseCertifications = new ArrayList<>();

    private List<LinkedinVolunteerExperience> linkedinVolunteerExperiences = new ArrayList<>();

    private List<LinkedinInterest> linkedinInterests = new ArrayList<>();

    private List<LinkedinSkill> linkedinSkills = new ArrayList<>();

    private List<LinkedinActivity> linkedinActivities = new ArrayList<>();
}
