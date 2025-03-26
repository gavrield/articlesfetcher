package com.newssummarizer.articlesfetcher.repository;

import java.util.Set;

public class RepositoryConst {
    public static final String SEQUENCE_NAME = "articles_sequence";
    public static final Set<String> VALID_ARTICLE_ENTITY_FIELDS =
            Set.of("id", "query", "url", "publishedAt", "author", "title", "summary");
}
