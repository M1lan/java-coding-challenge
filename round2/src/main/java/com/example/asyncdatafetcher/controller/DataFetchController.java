package com.example.asyncdatafetcher.controller;

import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.service.DataFetchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataFetchController {
    private final DataFetchService dataFetchService;

    public DataFetchController(DataFetchService dataFetchService) {
        this.dataFetchService = dataFetchService;
    }

    @GetMapping("/")
    public MergedData getMergedData() {
        return dataFetchService.fetchMergedData();
    }
}
