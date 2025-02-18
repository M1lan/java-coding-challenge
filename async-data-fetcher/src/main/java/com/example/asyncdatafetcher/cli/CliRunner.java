package com.example.asyncdatafetcher.cli;

import com.example.asyncdatafetcher.service.DataFetchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements CommandLineRunner {
  private final DataFetchService s;
  private final ObjectMapper om;

  /** Construct a new CliRunner with DataFetchService and ObjectMapper. */
  public CliRunner(DataFetchService s, ObjectMapper om) {
    this.s = s;
    this.om = om;
  }

  /** Execute the command lien runner and fetch merged data if "cli" argument is provided. */
  public void run(String... args) throws Exception {
    if (args.length > 0 && "cli".equalsIgnoreCase(args[0])) {
      var m = s.fetchMergedData().block();
      System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(m));
      System.exit(0);
    }
  }
}
