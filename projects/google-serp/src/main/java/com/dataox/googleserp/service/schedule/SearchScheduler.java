package com.dataox.googleserp.service.schedule;

import com.dataox.googleserp.service.run.SearchStarter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.scheduling", name = "enabled", havingValue = "true")
public class SearchScheduler {
    private final SearchStarter searchStarter;

    @Scheduled(cron = "${app.scheduling.search.not.searched.data}")
    public void searchNotSearchedInitialData() {
        searchStarter.startSearchForNotSearchedInitialData();
    }
}
