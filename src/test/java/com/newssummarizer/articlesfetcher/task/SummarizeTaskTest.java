package com.newssummarizer.articlesfetcher.task;

import com.google.genai.Models;
import com.google.genai.types.Candidate;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import org.apache.http.HttpException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SummarizeTaskTest {

    @Mock
    private Models geminiModels;

    @Mock
    private ArticlesRepository repository;

    @InjectMocks
    private SummarizeTask summarizeTask;

    @Test
    void testRun() throws IOException, HttpException {
        // Arrange
        ArticleEntity article1 = new ArticleEntity();
        article1.setUrl("http://example.com/article1");
        ArticleEntity article2 = new ArticleEntity();
        article2.setUrl("http://example.com/article2");

        List<ArticleEntity> articles = Arrays.asList(article1, article2);

        when(repository.findByFieldNotExists("summary")).thenReturn(articles);

        GenerateContentResponse response1 = GenerateContentResponse.builder()
                .candidates(List.of(Candidate.builder()
                        .content(Content.builder()
                                .parts(List.of(Part.builder()
                                        .text("Summary of article 1")
                                        .build()))
                                .build())
                        .build()))
                .build();

        GenerateContentResponse response2 = GenerateContentResponse.builder()
                .candidates(List.of(Candidate.builder()
                        .content(Content.builder()
                                .parts(List.of(Part.builder()
                                        .text("Summary of article 2")
                                        .build()))
                                .build())
                        .build()))
                .build();

        when(geminiModels.generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article1"), isNull()))
                .thenReturn(response1);
        when(geminiModels.generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article2"), isNull()))
                .thenReturn(response2);

        // Act
        summarizeTask.run();

        // Assert
        verify(repository, times(1)).findByFieldNotExists("summary");
        verify(geminiModels, times(1)).generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article1"), isNull());
        verify(geminiModels, times(1)).generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article2"), isNull());
        verify(repository, times(2)).saveAll(articles);

        assertEquals("Summary of article 1", article1.getSummary());
        assertEquals("Summary of article 2", article2.getSummary());
    }

    @Test
    void testRunWithIOException() throws IOException, HttpException {
        // Arrange
        ArticleEntity article = new ArticleEntity();
        article.setUrl("http://example.com/article");

        List<ArticleEntity> articles = Arrays.asList(article);

        when(repository.findByFieldNotExists("summary")).thenReturn(articles);
        when(geminiModels.generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article"), isNull()))
                .thenThrow(new IOException("Network error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> summarizeTask.run());

        verify(repository, times(1)).findByFieldNotExists("summary");
        verify(geminiModels, times(1)).generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article"), isNull());
    }

    @Test
    void testRunWithHttpException() throws IOException, HttpException {
        // Arrange
        ArticleEntity article = new ArticleEntity();
        article.setUrl("http://example.com/article");

        List<ArticleEntity> articles = Arrays.asList(article);

        when(repository.findByFieldNotExists("summary")).thenReturn(articles);
        when(geminiModels.generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article"), isNull()))
                .thenThrow(new HttpException("HTTP error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> summarizeTask.run());

        verify(repository, times(1)).findByFieldNotExists("summary");
        verify(geminiModels, times(1)).generateContent(eq("gemini-2.0-flash-001"), eq("Can you summarize this article http://example.com/article"), isNull());
    }
}