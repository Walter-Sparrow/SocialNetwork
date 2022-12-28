package com.dataart.secondmonth.job;

import com.dataart.secondmonth.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadLinksCrawlerJob {

    private final LinkService linkService;

    @Scheduled(cron = "${jobs.dead-links-crawler.cron}")
    @Transactional
    public void removeAllUnusedLinks() {
        final long linksCount = linkService.getCount();
        log.info("DeadLinksCrawlerJob has begun. The current number of rows in the table={}", linksCount);
        linkService.removeAllUnusedLinks();
        final long linksCountAfterJob = linkService.getCount();
        log.info("DeadLinksCrawlerJob's complete. Number of rows removed={}", linksCount - linksCountAfterJob);
    }

}
