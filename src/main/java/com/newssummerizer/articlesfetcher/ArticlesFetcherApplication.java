package com.newssummerizer.articlesfetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ArticlesFetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticlesFetcherApplication.class, args);
	}

}
