package com.newssummarizer.articlesfetcher.controller;

import com.newssummarizer.articlesfetcher.service.AdHocFetchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Article fetcher API", description = "Operations for fetching news articles and store them in a database")
public class AdHocFetchController {

    private final AdHocFetchService adHocFetchService;

    @GetMapping("/fetch")
    @Operation(summary = "Fetches news articles by query and store them in a database")
    public void fetchArticlesAndStore(@RequestParam @NotBlank String query) {
        adHocFetchService.fetch(query);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleRuntimeException(RuntimeException ex) {
        log.error("Error occurred while fetching articles", ex);
    }
}
