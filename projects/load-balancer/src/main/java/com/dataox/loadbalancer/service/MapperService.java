package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.domain.entities.LinkedinActivity;
import com.dataox.loadbalancer.domain.entities.LinkedinPost;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import lombok.experimental.UtilityClass;

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
}
