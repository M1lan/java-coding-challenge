package com.example.asyncdatafetcher.service;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiClientTest {

    @Test
    public void testGet() throws Exception {

		//mockito? :-)
		HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/test", new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					String response = "test response";
					exchange.sendResponseHeaders(200, response.length());
					try (OutputStream os = exchange.getResponseBody()) {
						os.write(response.getBytes());
					}
				}
			});
        server.start();

        int port = server.getAddress().getPort();
        String url = "http://localhost:" + port + "/test";
        ApiClient client = new ApiClient();
        String response = client.get(url);
        assertEquals("test response", response);

        server.stop(0);
    }
}
