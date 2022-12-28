package com.example.rickandmortyapp.service;

import java.io.IOException;
import com.example.rickandmortyapp.exception.DataProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

@Component
public class HttpClient {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpClient(CloseableHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public <T> T get(String url, Class<T> clazz) {
        HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return objectMapper.readValue(response.getEntity().getContent(), clazz);
        } catch (IOException e) {
            throw new DataProcessingException("Can't fetch data from url: " + url, e);
        }
    }
}
