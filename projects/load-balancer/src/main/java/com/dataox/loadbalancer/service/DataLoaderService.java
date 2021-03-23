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
            MapperService.reSetLinkedinProfile(linkedinProfile);
        }
        linkedinProfileRepository.saveAll(linkedinProfiles);
    }

}
