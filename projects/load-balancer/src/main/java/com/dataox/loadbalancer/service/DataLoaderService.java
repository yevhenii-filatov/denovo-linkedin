package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.LinkedinProfileRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import com.dataox.loadbalancer.domain.types.LinkedinJobType;
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
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DataLoaderService {
    LinkedinProfileRepository linkedinProfileRepository;
    SearchResultRepository searchResultRepository;

    public void saveLinkedinProfiles(List<LinkedinProfile> linkedinProfiles) {
        for (LinkedinProfile linkedinProfile : linkedinProfiles) {
            SearchResult searchResult = searchResultRepository.findByUrl(linkedinProfile.getProfileUrl());
            linkedinProfile.setSearchResult(searchResult);
//            linkedinProfile.getLinkedinEducations().forEach(linkedinEducation -> linkedinEducation.setLinkedinProfile(linkedinProfile));
//            linkedinProfile.getLinkedinExperiences().forEach(linkedinExperience -> linkedinExperience.setLinkedinProfile(linkedinProfile));
//            linkedinProfile.getLinkedinInterests().forEach(linkedinInterest -> linkedinInterest.setLinkedinProfile(linkedinProfile));
//            linkedinProfile.getLinkedinRecommendations().forEach(recommendation -> recommendation.setLinkedinProfile(linkedinProfile));
//            linkedinProfile.getLinkedinBasicProfileInfo().setLinkedinProfile(linkedinProfile);
        }
        linkedinProfileRepository.saveAll(linkedinProfiles);
    }
}
