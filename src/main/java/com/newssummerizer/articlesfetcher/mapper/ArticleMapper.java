package com.newssummerizer.articlesfetcher.mapper;

import com.kwabenaberko.newsapilib.models.Article;
import com.newssummerizer.articlesfetcher.repository.ArticleEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ArticleMapper {

    List<ArticleEntity> toArticleEntityList(List<Article> articles);
}
