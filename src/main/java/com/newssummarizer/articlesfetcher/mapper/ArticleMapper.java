package com.newssummarizer.articlesfetcher.mapper;

import com.kwabenaberko.newsapilib.models.Article;
import com.newssummarizer.articlesfetcher.repository.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "_id", ignore = true)
    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "query", ignore = true)
    List<ArticleEntity> toArticleEntityList(List<Article> articles);
}
