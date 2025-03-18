package com.newssummarizer.articlesfetcher.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TasksCollection {
    @Autowired
    private List<FetchTask> fetchTaskList;

    public List<FetchTask> getFetchTaskList() {
        return this.fetchTaskList;
    }
}
