package com.analysis.stock.service.impl;

import com.analysis.stock.model.Stock;
import com.analysis.stock.repository.StockRepository;
import com.analysis.stock.service.StockService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Override
    public List<Stock> getAllStocksByIssuerName(String name) {
        return stockRepository.findAllByName(name);
    }
}
