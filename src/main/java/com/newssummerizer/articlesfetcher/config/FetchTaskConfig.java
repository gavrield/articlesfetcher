package com.newssummerizer.articlesfetcher.config;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.newssummerizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummerizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummerizer.articlesfetcher.repository.SequenceGeneratorService;
import com.newssummerizer.articlesfetcher.task.FetchTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class FetchTaskConfig {

    @Bean
    public FetchTask fetchTaskBusiness(NewsApiClient client, ArticlesRepository repository, ArticleMapper mapper, SequenceGeneratorService sequenceGeneratorService) {
        return new FetchTask("Business", LocalDate.now(), repository, client, mapper, sequenceGeneratorService);
    }

    @Bean
    public FetchTask fetchTaskMiddleEast(NewsApiClient client, ArticlesRepository repository, ArticleMapper mapper, SequenceGeneratorService sequenceGeneratorService) {
        return new FetchTask("Middle East", LocalDate.now(), repository, client, mapper, sequenceGeneratorService);
    }

    @Bean
    public FetchTask fetchTaskSports(NewsApiClient client, ArticlesRepository repository, ArticleMapper mapper, SequenceGeneratorService sequenceGeneratorService) {
        return new FetchTask("Sports", LocalDate.now(), repository, client, mapper, sequenceGeneratorService);
    }

}
