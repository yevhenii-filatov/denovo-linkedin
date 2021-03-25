package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.LinkedinNotReusableProfileRepository;
import com.dataox.loadbalancer.domain.repositories.LinkedinProfileRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import com.dataox.loadbalancer.exception.RecordNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.Objects.isNull;

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
    LinkedinNotReusableProfileRepository notReusableProfileRepository;

    public void saveLinkedinProfiles(List<LinkedinProfile> linkedinProfiles) {
        for (LinkedinProfile scrapedProfile : linkedinProfiles) {
            Long searchResultId = scrapedProfile.getSearchResult().getId();
            SearchResult searchResult = searchResultRepository.findById(searchResultId)
                    .orElseThrow(() -> new RecordNotFoundException("Search result with searchResultId " + searchResultId + " not found in database"));
            if (isNull(searchResult.getLinkedinProfile())) {
                initialSave(scrapedProfile, searchResult);
            } else {
                updateSave(scrapedProfile, searchResult);
            }
        }
    }

    private void updateSave(LinkedinProfile scrapedProfile, SearchResult searchResult) {
        LinkedinProfile profileToUpdate = searchResult.getLinkedinProfile();
        MapperService.reSetScrapedOptionalFields(profileToUpdate, scrapedProfile);
        MapperService.reSetRequiredFields(profileToUpdate, scrapedProfile);
        MapperService.reSetLinkedinProfile(profileToUpdate);
        profileToUpdate.setUpdatedAt(Instant.now());
        linkedinProfileRepository.save(profileToUpdate);
    }

    private void initialSave(LinkedinProfile scrapedProfile, SearchResult searchResult) {
        scrapedProfile.setSearchResult(searchResult);
        MapperService.reSetLinkedinProfile(scrapedProfile);
        linkedinProfileRepository.save(scrapedProfile);
    }

    public void saveNotReusableProfiles(List<LinkedinNotReusableProfile> notReusableProfiles) {
        notReusableProfileRepository.saveAll(notReusableProfiles);
    }
}
