package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.exception.DataNotFoundException;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Lysko
 * @since 25/03/2021
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScrapingDataValidationService {

    NotificationsService notificationsService;

    public void validateInitialData(final List<Long> denovoIds, List<InitialData> initialData) {
        if (initialData.isEmpty())
            throw new DataNotFoundException("Initial data, with given denovo ids: " + denovoIds + " not found");
        List<Long> notFoundDenovoIds = checkForNotFoundDenovoIds(denovoIds, initialData);
        List<Long> moreThanOneSearchResultDenovoIds = checkSearchResults(initialData);
        if (!notFoundDenovoIds.isEmpty())
            notificationsService.sendAll(String.format("LoadBalancer: Denovo ids %s was not found in database", notFoundDenovoIds));
        if (!moreThanOneSearchResultDenovoIds.isEmpty())
            notificationsService.sendAll(String.format("LoadBalancer: Initial data with given denovo ids %s has more than one search result", moreThanOneSearchResultDenovoIds));
    }

    private List<Long> checkSearchResults(List<InitialData> initialData) {
        return initialData.stream()
                .filter(initData -> initData.getSearchResults().size() > 1)
                .map(InitialData::getDenovoId)
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Long> checkForNotFoundDenovoIds(List<Long> denovoIds, List<InitialData> initialData) {
        List<Long> denovoIdsFromInitialData = initialData.stream()
                .map(InitialData::getDenovoId)
                .collect(Collectors.toList());
        return denovoIds.stream()
                .filter(aLong -> !denovoIdsFromInitialData.contains(aLong))
                .sorted()
                .collect(Collectors.toList());
    }

    public void validateUpdateProfiles(List<LinkedinProfile> toUpdateProfiles, List<Long> linkedinProfileIds) {
        if (toUpdateProfiles.isEmpty())
            throw new DataNotFoundException("Linkedin profiles, with given ids " + linkedinProfileIds + " was not found");
        List<Long> notFoundProfilesIds = checkNotFoundProfiles(toUpdateProfiles, linkedinProfileIds);
        if (!notFoundProfilesIds.isEmpty())
            notificationsService.sendAll(String.format("Linkedin profiles with ids %s was not found", notFoundProfilesIds));
    }

    private List<Long> checkNotFoundProfiles(List<LinkedinProfile> toUpdateProfiles, List<Long> linkedinProfileIds) {
        List<Long> profilesIds = toUpdateProfiles.stream()
                .map(LinkedinProfile::getId)
                .collect(Collectors.toList());
        return linkedinProfileIds.stream()
                .filter(aLong -> !profilesIds.contains(aLong))
                .collect(Collectors.toList());
    }
}
