package com.newssummarizer.articlesfetcher.controller;

import com.newssummarizer.articlesfetcher.service.AdHocFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdHocFetchController {

    @Autowired
    private AdHocFetchService adHocFetchService;

    @GetMapping("/fetch")
    public void fetchArticlesAndStore(@RequestParam String query) {
        adHocFetchService.fetch(query);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleRuntimeException(RuntimeException ex) {
        System.err.println(ex.getMessage());
    }
}
