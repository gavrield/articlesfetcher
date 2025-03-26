package com.newssummarizer.articlesfetcher.task;

import com.google.genai.Models;
import com.google.genai.types.GenerateContentResponse;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.List;

@Slf4j
public class SummarizeTask implements Runnable {

    private final Models geminiModels;
    private final ArticlesRepository repository;

    private final String geminiModel = "gemini-2.0-flash-001";
    private final String baseQuery = "Can you summarize this article ";

    public SummarizeTask(Models models,ArticlesRepository repository) {
        this.geminiModels = models;
        this.repository = repository;
    }

    @Override
    public void run() {

        if (Thread.currentThread().isInterrupted()) {
            log.warn("Summarize articles task was interrupted. Stopping execution");
            return;
        }
        List<ArticleEntity> articleEntities = repository.findByMissingField("summary");
        for (ArticleEntity entity: articleEntities) {
            try {
                GenerateContentResponse response = geminiModels.generateContent(
                        geminiModel,
                        baseQuery + entity.getUrl(),
                        null);
                entity.setSummary(response.text());
            } catch (IOException | HttpException e) {
                log.error("Gemini request failed", e);
                throw new RuntimeException(e);
            } finally {
                repository.saveAll(articleEntities);
                log.info("{} articles were summarized", articleEntities.size());
            }
        }
    }
}
