package com.newssummarizer.articlesfetcher.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TasksCollection {

    private final List<FetchTask> fetchTaskList;

    public synchronized List<FetchTask>  getFetchTaskList() {
        return Collections.unmodifiableList(this.fetchTaskList);
    }

    public synchronized void addTask(FetchTask task) {
        this.fetchTaskList.add(task);
    }

    public synchronized FetchTask removeTask(int index) {
        return fetchTaskList.remove(index);
    }
}
