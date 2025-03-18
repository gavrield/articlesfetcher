package com.newssummarizer.articlesfetcher.controller;

import com.newssummarizer.articlesfetcher.service.AdHocFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdHocFetchController {

    @Autowired
    private AdHocFetchService adHocFetchService;

    @GetMapping("/fetch")
    public void fetchArticlesAndStore(@RequestParam String query) {
        adHocFetchService.fetch(query);
    }
}
