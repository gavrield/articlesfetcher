package com.newssummarizer.articlesfetcher.service;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.newssummarizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.repository.SequenceGeneratorService;
import com.newssummarizer.articlesfetcher.task.FetchTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

@Service
public class AdHocFetchService {

    @Autowired
    private ArticlesRepository repository;
    @Autowired
    private NewsApiClient client;
    @Autowired
    private ArticleMapper mapper;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private SummarizeService summarizeService;

    public void fetch(String query) {
        FetchTask task = new FetchTask(
                query,
                LocalDate.now(),
                repository,
                client,
                mapper,
                sequenceGeneratorService
        );
        task.run();
    }
}
