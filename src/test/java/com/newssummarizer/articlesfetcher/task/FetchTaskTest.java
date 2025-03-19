package com.newssummarizer.articlesfetcher.task;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.newssummarizer.articlesfetcher.mapper.ArticleMapper;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import com.newssummarizer.articlesfetcher.repository.ArticlesRepository;
import com.newssummarizer.articlesfetcher.repository.SequenceGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchTaskTest {

    @Mock
    private NewsApiClient newsApiClient;

    @Mock
    private ArticlesRepository repository;

    @Mock
    private ArticleMapper mapper;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @InjectMocks
    private FetchTask fetchTask;

    @Captor
    private ArgumentCaptor<EverythingRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<ArticleEntity> articleEntityCaptor;

    private final String query = "test query";
    private final LocalDate from = LocalDate.now();

    @BeforeEach
    void setUp() {
        fetchTask = new FetchTask(query, from, repository, newsApiClient, mapper, sequenceGeneratorService);
    }

    @Test
    void testRunSuccess() {
        // Arrange
        ArticleResponse mockResponse = mock(ArticleResponse.class);
        Article mockArticle = mock(Article.class);
        ArticleEntity mockArticleEntity = mock(ArticleEntity.class);

        // Mock the response and mapper behavior
        when(mockResponse.getArticles()).thenReturn(Collections.singletonList(mockArticle));
        when(mapper.toArticleEntityList(anyList())).thenReturn(Collections.singletonList(mockArticleEntity));
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);

        // Use doAnswer to simulate the callback invocation
        doAnswer(invocation -> {
            NewsApiClient.ArticlesResponseCallback callback = invocation.getArgument(1);
            callback.onSuccess(mockResponse); // Trigger the onSuccess callback
            return null;
        }).when(newsApiClient).getEverything(any(EverythingRequest.class), any(NewsApiClient.ArticlesResponseCallback.class));

        // Act
        fetchTask.run();

        // Assert
        verify(newsApiClient).getEverything(requestCaptor.capture(), any(NewsApiClient.ArticlesResponseCallback.class));
        assertEquals(query, requestCaptor.getValue().getQ());
        assertEquals(from.minusDays(1).toString(), requestCaptor.getValue().getFrom());
        assertEquals("en", requestCaptor.getValue().getLanguage());

        // Verify that the mapper and repository methods were called
        verify(mapper).toArticleEntityList(Collections.singletonList(mockArticle));
        verify(sequenceGeneratorService).generateSequence(ArticleEntity.SEQUENCE_NAME);
        verify(repository).insert(mockArticleEntity);
    }

    @Test
    void testRunFailure() {
        // Arrange
        Throwable mockThrowable = mock(Throwable.class);
        doAnswer(invocation -> {
            NewsApiClient.ArticlesResponseCallback callback = invocation.getArgument(1);
            callback.onFailure(mockThrowable);
            return null;
        }).when(newsApiClient).getEverything(any(EverythingRequest.class), any(NewsApiClient.ArticlesResponseCallback.class));

        // Act & Assert
        try {
            fetchTask.run();
        } catch (RuntimeException e) {
            assertEquals(mockThrowable, e.getCause());
        }

        verify(newsApiClient).getEverything(any(EverythingRequest.class), any(NewsApiClient.ArticlesResponseCallback.class));
        verifyNoInteractions(mapper, sequenceGeneratorService, repository);
    }
}