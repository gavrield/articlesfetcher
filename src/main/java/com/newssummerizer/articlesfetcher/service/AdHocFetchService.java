package com.newssummerizer.articlesfetcher.service;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.newssummerizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummerizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummerizer.articlesfetcher.repository.SequenceGeneratorService;
import com.newssummerizer.articlesfetcher.task.FetchTask;
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
