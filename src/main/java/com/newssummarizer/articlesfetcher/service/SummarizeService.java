package com.newssummarizer.articlesfetcher.service;

import com.newssummarizer.articlesfetcher.task.SummarizeTask;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SummarizeService {
    
    private final ThreadPoolTaskScheduler taskScheduler;
    
    private final SummarizeTask task;
    
    private final Duration duration;

    private ScheduledFuture<?> scheduledFuture;

    @PostConstruct
    public void summarizeArticles() {
        this.scheduledFuture = taskScheduler.scheduleWithFixedDelay(task, duration);
    }

    @PreDestroy
    public void shutDown() {
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
        }
    }
}
