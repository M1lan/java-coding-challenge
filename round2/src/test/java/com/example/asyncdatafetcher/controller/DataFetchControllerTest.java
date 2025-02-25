package com.example.asyncdatafetcher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {
    @GetMapping("/error")
    public String throwException() {
        throw new RuntimeException("forced error");
    }
}
