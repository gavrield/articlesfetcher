package com.newssummarizer.articlesfetcher.service;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.newssummarizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.repository.SequenceGeneratorService;
import com.newssummarizer.articlesfetcher.task.FetchTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AdHocFetchService {


    private final ArticlesRepository repository;

    private final NewsApiClient client;

    private final ArticleMapper mapper;

    private final SequenceGeneratorService sequenceGeneratorService;

    public void fetch(String query) {
        FetchTask task = new FetchTask(
                query,
                LocalDate.now(),
                repository,
                client,
                mapper,
                sequenceGeneratorService
        );
        CompletableFuture.runAsync(task);
    }
}
