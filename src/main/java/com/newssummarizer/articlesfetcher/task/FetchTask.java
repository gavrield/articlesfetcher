package com.newssummarizer.articlesfetcher.task;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.newssummarizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.repository.SequenceGeneratorService;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

public class FetchTask implements Runnable {
    private NewsApiClient newsApiClient;
    private String query;
    private LocalDate from;
    private ArticlesRepository repository;
    private ArticleMapper mapper;
    private SequenceGeneratorService sequenceGeneratorService;

    public FetchTask(String query, LocalDate from, ArticlesRepository repository, NewsApiClient newsApiClient, ArticleMapper mapper, SequenceGeneratorService sequenceGeneratorService) {
        this.query = query;
        this.from = from;
        this.repository = repository;
        this.newsApiClient = newsApiClient;
        this.mapper = mapper;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public void run() {
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q(query)
                        .from(from.minusDays(1).toString())
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse articleResponse) {
                        List<Article> articles = articleResponse.getArticles();
                        List<ArticleEntity> articleEntities = mapper.toArticleEntityList(articles);
                        articleEntities.forEach(articleEntity ->
                                articleEntity.set_id(BigInteger.valueOf(
                                        sequenceGeneratorService.generateSequence(ArticleEntity.SEQUENCE_NAME))));
                        for (ArticleEntity articleEntity: articleEntities) {
                            repository.insert(articleEntity);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                }
        );
    }
}
