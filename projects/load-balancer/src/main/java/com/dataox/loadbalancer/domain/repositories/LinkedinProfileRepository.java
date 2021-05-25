package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

public interface LinkedinProfileRepository extends JpaRepository<LinkedinProfile, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE LinkedinProfile l " +
            "SET l.profileUrl = :profileUrl," +
            "    l.linkedinAccomplishments  = :linkedinAccomplishments, " +
            "    l.searchResult  = :searchResult, " +
            "    l.linkedinActivities  = :linkedinActivities, " +
            "    l.linkedinBasicProfileInfo  = :linkedinBasicProfileInfo, " +
            "    l.linkedinEducations  = :linkedinEducations, " +
            "    l.linkedinExperiences  = :linkedinExperiences, " +
            "    l.linkedinInterests  = :linkedinInterests, " +
            "    l.linkedinLicenseCertifications  = :linkedinLicenseCertifications, " +
            "    l.linkedinRecommendations  = :linkedinRecommendations, " +
            "    l.linkedinSkills  = :linkedinSkills, " +
            "    l.linkedinVolunteerExperiences  = :linkedinVolunteerExperiences, " +
            "    l.updatedAt  = :updatedAt " +
            "WHERE l.id = :id")
    void updateLinkedinProfile(@Param("profileUrl") String profileUrl,
                               @Param("linkedinAccomplishments") List<LinkedinAccomplishment> linkedinAccomplishments,
                               @Param("searchResult") SearchResult searchResult,
                               @Param("linkedinActivities") List<LinkedinActivity> linkedinActivities,
                               @Param("linkedinBasicProfileInfo") LinkedinBasicProfileInfo linkedinBasicProfileInfo,
                               @Param("linkedinEducations") List<LinkedinEducation> linkedinEducations,
                               @Param("linkedinExperiences") List<LinkedinExperience> linkedinExperiences,
                               @Param("linkedinInterests") List<LinkedinInterest> linkedinInterests,
                               @Param("linkedinLicenseCertifications") List<LinkedinLicenseCertification> linkedinLicenseCertifications,
                               @Param("linkedinRecommendations") List<LinkedinRecommendation> linkedinRecommendations,
                               @Param("linkedinSkills") List<LinkedinSkill> linkedinSkills,
                               @Param("linkedinVolunteerExperiences") List<LinkedinVolunteerExperience> linkedinVolunteerExperiences,
                               @Param("updatedAt") Instant updatedAt,
                               @Param("id") Long id);

    default void updateLinkedinProfile(LinkedinProfile p) {

        updateLinkedinProfile(  p.getProfileUrl(),
                                p.getLinkedinAccomplishments(),
                                p.getSearchResult(),
                                p.getLinkedinActivities(),
                                p.getLinkedinBasicProfileInfo(),
                                p.getLinkedinEducations(),
                                p.getLinkedinExperiences(),
                                p.getLinkedinInterests(),
                                p.getLinkedinLicenseCertifications(),
                                p.getLinkedinRecommendations(),
                                p.getLinkedinSkills(),
                                p.getLinkedinVolunteerExperiences(),
                                p.getUpdatedAt(),
                                p.getId());
    }

}
