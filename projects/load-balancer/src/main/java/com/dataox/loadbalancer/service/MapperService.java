package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.domain.entities.*;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 23/03/2021
 */
@UtilityClass
public class MapperService {

    public void reSetLinkedinProfile(LinkedinProfile linkedinProfile) {
        linkedinProfile.getLinkedinAccomplishments().forEach(linkedinAccomplishment -> linkedinAccomplishment.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinActivities().forEach(linkedinActivity -> linkedinActivity.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinBasicProfileInfo().setLinkedinProfile(linkedinProfile);
        linkedinProfile.getLinkedinEducations().forEach(linkedinEducation -> linkedinEducation.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinExperiences().forEach(linkedinExperience -> linkedinExperience.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinInterests().forEach(linkedinInterest -> linkedinInterest.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinLicenseCertifications().forEach(linkedinLicenseCertification -> linkedinLicenseCertification.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinRecommendations().forEach(recommendation -> recommendation.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinSkills().forEach(linkedinSkill -> linkedinSkill.setLinkedinProfile(linkedinProfile));
        linkedinProfile.getLinkedinVolunteerExperiences().forEach(linkedinVolunteerExperience -> linkedinVolunteerExperience.setLinkedinProfile(linkedinProfile));
        for (LinkedinActivity linkedinActivity : linkedinProfile.getLinkedinActivities()) {
            linkedinActivity.setLinkedinProfile(linkedinProfile);
            LinkedinPost linkedinPost = linkedinActivity.getLinkedinPost();
            linkedinPost.setLinkedinActivity(linkedinActivity);
            linkedinPost.getLinkedinComments().forEach(linkedinComment -> linkedinComment.setLinkedinPost(linkedinPost));
        }
    }

    public void reSetScrapedOptionalFields(LinkedinProfile profileToUpdate, LinkedinProfile reScrapedProfile) {
        List<LinkedinLicenseCertification> linkedinLicenseCertifications = reScrapedProfile.getLinkedinLicenseCertifications();
        if (nonNull(linkedinLicenseCertifications) && !linkedinLicenseCertifications.isEmpty()) {
            List<LinkedinLicenseCertification> licenseCertifications = profileToUpdate.getLinkedinLicenseCertifications();
            licenseCertifications.clear();
            licenseCertifications.addAll(linkedinLicenseCertifications);
        }

        List<LinkedinVolunteerExperience> linkedinVolunteerExperiences = reScrapedProfile.getLinkedinVolunteerExperiences();
        if (nonNull(linkedinVolunteerExperiences) && !linkedinVolunteerExperiences.isEmpty()) {
            List<LinkedinVolunteerExperience> volunteerExperiences = profileToUpdate.getLinkedinVolunteerExperiences();
            volunteerExperiences.clear();
            volunteerExperiences.addAll(linkedinVolunteerExperiences);
        }

        List<LinkedinInterest> linkedinInterests = reScrapedProfile.getLinkedinInterests();
        if (nonNull(linkedinInterests) && !linkedinInterests.isEmpty()) {
            List<LinkedinInterest> interests = profileToUpdate.getLinkedinInterests();
            interests.clear();
            interests.addAll(linkedinInterests);
        }

        List<LinkedinRecommendation> linkedinRecommendations = reScrapedProfile.getLinkedinRecommendations();
        if (nonNull(linkedinRecommendations) && !linkedinRecommendations.isEmpty()) {
            List<LinkedinRecommendation> recommendations = profileToUpdate.getLinkedinRecommendations();
            recommendations.clear();
            recommendations.addAll(linkedinRecommendations);
        }

        List<LinkedinAccomplishment> linkedinAccomplishments = reScrapedProfile.getLinkedinAccomplishments();
        if (nonNull(linkedinAccomplishments) && !linkedinAccomplishments.isEmpty()) {
            List<LinkedinAccomplishment> accomplishments = profileToUpdate.getLinkedinAccomplishments();
            accomplishments.clear();
            accomplishments.addAll(linkedinAccomplishments);
        }

        List<LinkedinSkill> linkedinSkills = reScrapedProfile.getLinkedinSkills();
        if (nonNull(linkedinSkills) && !linkedinSkills.isEmpty()) {
            List<LinkedinSkill> skills = profileToUpdate.getLinkedinSkills();
            skills.clear();
            skills.addAll(linkedinSkills);
        }

        List<LinkedinActivity> linkedinActivities = reScrapedProfile.getLinkedinActivities();
        if (nonNull(linkedinActivities) && !linkedinActivities.isEmpty()) {
            List<LinkedinActivity> activities = profileToUpdate.getLinkedinActivities();
            activities.clear();
            activities.addAll(linkedinActivities);
        }
    }

    public void reSetRequiredFields(LinkedinProfile profileToUpdate, LinkedinProfile reScrapedProfile) {
        profileToUpdate.setLinkedinBasicProfileInfo(reScrapedProfile.getLinkedinBasicProfileInfo());

        List<LinkedinExperience> linkedinExperiences = profileToUpdate.getLinkedinExperiences();
        linkedinExperiences.clear();
        linkedinExperiences.addAll(reScrapedProfile.getLinkedinExperiences());

        List<LinkedinEducation> linkedinEducations = profileToUpdate.getLinkedinEducations();
        linkedinEducations.clear();
        linkedinEducations.addAll(reScrapedProfile.getLinkedinEducations());

    }
}
