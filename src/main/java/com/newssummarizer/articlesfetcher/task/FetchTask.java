package com.newssummarizer.articlesfetcher.task;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.newssummarizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.repository.RepositoryConst;
import com.newssummarizer.articlesfetcher.repository.SequenceGeneratorService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class FetchTask implements Runnable {
    private final NewsApiClient newsApiClient;
    private final String query;
    private final LocalDate from;
    private final ArticlesRepository repository;
    private final ArticleMapper mapper;
    private final SequenceGeneratorService sequenceGeneratorService;

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
        if (Thread.currentThread().isInterrupted()) {
            log.warn("Fetch with query '{}' articles task was interrupted. Stopping execution", this.query);
            return;
        }
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
                        articleEntities.forEach(articleEntity -> {
                            articleEntity.set_id(BigInteger.valueOf(
                                    sequenceGeneratorService.generateSequence(RepositoryConst.SEQUENCE_NAME)));
                            articleEntity.setQuery(query);
                        });
                        for (ArticleEntity articleEntity: articleEntities) {
                            repository.insert(articleEntity);
                        }
                        log.info("Fetched and saves {} articles for query '{}'",articleEntities.size(), query);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("Error fetching articles for query '{}'", query, throwable);
                        throw new RuntimeException(throwable);
                    }
                }
        );
    }
}
