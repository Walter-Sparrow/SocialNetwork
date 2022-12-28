package com.dataart.secondmonth.job;

import com.dataart.secondmonth.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredConfirmationTokensCrawlerJob {

    private final ConfirmationTokenService confirmationTokenService;

    @Transactional
    @Scheduled(cron = "${jobs.expired-confirmation-tokes-crawler.cron}")
    public void removeExpiredConfirmationTokens() {
        final long tokensCount = confirmationTokenService.getCount();
        log.info("ExpiredConfirmationTokensCrawlerJob has begun. The current number of rows in the table={}", tokensCount);
        confirmationTokenService.removeExpiredTokens();
        final long tokensCountAfterJob = confirmationTokenService.getCount();
        log.info("ExpiredConfirmationTokensCrawlerJob's complete. Number of rows removed={}", tokensCount - tokensCountAfterJob);
    }

}
