package com.newssummarizer.articlesfetcher.service;

import com.newssummarizer.articlesfetcher.task.SummarizeTask;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SummarizeService {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private SummarizeTask task;
    @Autowired
    private Duration duration;

    @PostConstruct
    public void summarizeArticles() {
        var future = taskScheduler.scheduleWithFixedDelay(task, duration);
    }
}
