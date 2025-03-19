package com.newssummarizer.articlesfetcher.controller;

import com.newssummarizer.articlesfetcher.service.AdHocFetchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdHocFetchControllerTest {

    @Mock
    private AdHocFetchService adHocFetchService;

    @InjectMocks
    private AdHocFetchController adHocFetchController;

    @Test
    void fetchArticlesAndStore_shouldCallServiceWithQuery() throws Exception {
        String query = "test query";

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(adHocFetchController).build();

        mockMvc.perform(get("/fetch").param("query", query))
                .andExpect(status().isOk());

        verify(adHocFetchService, times(1)).fetch(query);
    }

    @Test
    void fetchArticlesAndStore_serviceThrowsException_shouldReturnInternalServerError() throws Exception {
        String query = "test query";
        doThrow(new RuntimeException("Service failure")).when(adHocFetchService).fetch(query);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(adHocFetchController).build();

        mockMvc.perform(get("/fetch").param("query", query))
                .andExpect(status().isInternalServerError());

        verify(adHocFetchService, times(1)).fetch(query);
    }

    @Test
    void fetchArticlesAndStore_missingQueryParameter_shouldReturnBadRequest() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(adHocFetchController).build();

        mockMvc.perform(get("/fetch"))
                .andExpect(status().isBadRequest());

        verify(adHocFetchService, never()).fetch(anyString());
    }
}