package com.example.asyncdatafetcher.cli;

import com.example.asyncdatafetcher.service.DataFetchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements CommandLineRunner {

    private final DataFetchService dataFetchService;
    private final ObjectMapper objectMapper;

    public CliRunner(DataFetchService dataFetchService, ObjectMapper objectMapper) {
        this.dataFetchService = dataFetchService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && "cli".equalsIgnoreCase(args[0])) {
            var mergedData = dataFetchService.fetchMergedData();
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mergedData));
            System.exit(0);
        }
    }
}
