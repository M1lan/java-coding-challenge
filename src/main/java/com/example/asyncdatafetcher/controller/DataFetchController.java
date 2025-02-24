package com.example.asyncdatafetcher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataFetchController {
    @GetMapping("/")
    public String getMessage() {
        return "hello world";
    }
}
