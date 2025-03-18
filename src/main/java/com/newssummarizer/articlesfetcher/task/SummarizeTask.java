package com.newssummarizer.articlesfetcher.task;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.List;

public class SummarizeTask implements Runnable{

    private Client geminiClient;
    private ArticlesRepository repository;

    private final String geminiModel = "gemini-2.0-flash-001";
    private final String baseQuery = "Can you summarize this article ";

    public SummarizeTask(Client geminiClient,ArticlesRepository repository) {
        this.geminiClient = geminiClient;
        this.repository = repository;
    }

    @Override
    public void run() {
        List<ArticleEntity> articleEntities = repository.findByFieldNotExists("summary");
        for (ArticleEntity entity: articleEntities) {
            try {
                GenerateContentResponse response = geminiClient.models.generateContent(
                        geminiModel,
                        baseQuery + entity.getUrl(),
                        null);
                entity.setSummary(response.text());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (HttpException e) {
                throw new RuntimeException(e);
            } finally {
                repository.saveAll(articleEntities);
            }
        }
    }
}
