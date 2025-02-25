package com.example.asyncdatafetcher.controller;
import com.example.asyncdatafetcher.model.MergedData;
import com.example.asyncdatafetcher.service.DataFetchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest public class DataFetchControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private DataFetchService dataFetchService;
	@Test public void testGetMergedData() throws Exception {
		MergedData mergedData = dataFetchService.fetchMergedData();
		mockMvc.perform(get("/")) .andExpect(status().isOk()) .andExpect(jsonPath("$.user").exists()) .andExpect(jsonPath("$.posts").isArray());
	}
}
