package com.newssummarizer.articlesfetcher.config;

import com.kwabenaberko.newsapilib.NewsApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NewsApiConfig {

    @Value("${news.api.key}")
    private String newsApiKey;

    @Bean
    public NewsApiClient newsApiClient() {
        return new NewsApiClient(newsApiKey);
    }
}
