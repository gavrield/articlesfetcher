package com.newssummarizer.articlesfetcher.config;


import com.google.genai.Client;
import com.google.genai.Models;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.task.SummarizeTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Bean
    public Models geminiClient() {
        return new Client().models;
    }

    @Bean
    public SummarizeTask summarizeTask(Models models, ArticlesRepository repository) {
        return new SummarizeTask(models, repository);
    }
}
