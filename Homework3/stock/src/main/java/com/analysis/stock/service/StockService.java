package com.analysis.stock.service;

import com.analysis.stock.model.Stock;

import java.util.List;
import java.util.Map;

public interface StockService {
    List<Stock> getAllStocksByIssuerName(String name);
    void analyzeStock(String stockName, String startDate, String endDate);
}