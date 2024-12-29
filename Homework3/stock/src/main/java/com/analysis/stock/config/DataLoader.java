package com.analysis.stock.config;

import com.analysis.stock.model.Stock;
import com.analysis.stock.repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.asm.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    private static final String DATE_PATTERN = "MM/dd/yyyy";
    private final ObjectMapper objectMapper;
    private final StockRepository stockRepository;

    public DataLoader(ObjectMapper objectMapper, StockRepository stockRepository) {
        this.objectMapper = objectMapper;
        this.stockRepository = stockRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Stock> posts = new ArrayList<>();
        JsonNode json;

        try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/stocks.json")) {
            json = objectMapper.readValue(inputStream, JsonNode.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data", e);
        }

        System.out.println(json);
    }
}
