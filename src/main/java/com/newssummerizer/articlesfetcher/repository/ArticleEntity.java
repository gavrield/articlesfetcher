package com.newssummerizer.articlesfetcher.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = "articles")
@Data
public class ArticleEntity {

    @Transient
    public static final String SEQUENCE_NAME = "articles_sequence";

    @Id
    private BigInteger _id;
    private String title;
    private String author;
    private String publishedAt;
    private String url;
    private String summery;
}
