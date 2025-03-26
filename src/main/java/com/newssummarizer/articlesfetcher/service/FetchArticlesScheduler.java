package com.newssummarizer.articlesfetcher.service;

import com.newssummarizer.articlesfetcher.task.FetchTask;
import com.newssummarizer.articlesfetcher.task.TasksCollection;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchArticlesScheduler {
    
    private final ThreadPoolTaskScheduler taskScheduler;
    
    private final TasksCollection tasksCollection;
    
    private final CronTrigger cronTrigger;

    @PostConstruct
    public void fetchArticlesAndStoreThem () {
        for(FetchTask task: tasksCollection.getFetchTaskList()) {
            taskScheduler.schedule(task, cronTrigger);
        }
    }
}
