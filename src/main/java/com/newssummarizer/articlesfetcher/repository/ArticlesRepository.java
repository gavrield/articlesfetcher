package com.newssummarizer.articlesfetcher.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ArticlesRepository extends MongoRepository<ArticleEntity, BigInteger> {

    @Query("{ ?0 : { $exists: false } }")
    List<ArticleEntity> findByFieldNotExists(String fieldName);
}
