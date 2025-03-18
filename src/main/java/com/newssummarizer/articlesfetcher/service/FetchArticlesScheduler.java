package com.newssummarizer.articlesfetcher.service;

import com.newssummarizer.articlesfetcher.task.FetchTask;
import com.newssummarizer.articlesfetcher.task.TasksCollection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class FetchArticlesScheduler {
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private TasksCollection tasksCollection;
    @Autowired
    private CronTrigger cronTrigger;
    @Autowired
    private SummarizeService summarizeService;

    @PostConstruct
    public void fetchArticlesAndStoreThem () {
        for(FetchTask task: tasksCollection.getFetchTaskList()) {
            taskScheduler.schedule(task, cronTrigger);
        }
    }
}
