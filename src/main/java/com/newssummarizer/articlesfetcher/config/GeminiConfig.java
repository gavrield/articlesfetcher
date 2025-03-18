package com.newssummarizer.articlesfetcher.config;


import com.google.genai.Client;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.task.SummarizeTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Bean
    public Client geminiClient() {
        return new Client();
    }



    @Bean
    public SummarizeTask summarizeTask(Client client, ArticlesRepository repository) {
        return new SummarizeTask(client, repository);
    }
}
