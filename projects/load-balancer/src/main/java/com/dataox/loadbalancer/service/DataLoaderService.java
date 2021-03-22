package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.LinkedinProfileRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import com.dataox.loadbalancer.exception.RecordNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataLoaderService {
    LinkedinProfileRepository linkedinProfileRepository;
    SearchResultRepository searchResultRepository;

    public void saveLinkedinProfiles(List<LinkedinProfile> linkedinProfiles) {
        for (LinkedinProfile linkedinProfile : linkedinProfiles) {
            Long searchResultId = linkedinProfile.getSearchResult().getId();
            SearchResult searchResult = searchResultRepository.findById(searchResultId)
                    .orElseThrow(() -> new RecordNotFoundException("Search result with searchResultId " + searchResultId + " not found in database"));
            linkedinProfile.setSearchResult(searchResult);
            reSetLinkedinProfile(linkedinProfile);
        }
        linkedinProfileRepository.saveAll(linkedinProfiles);
    }

    private void reSetLinkedinProfile(LinkedinProfile linkedinProfile) {
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
    }
}
