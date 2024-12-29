package com.analysis.stock.service;

import com.analysis.stock.model.Stock;

import java.util.List;

public interface StockService {
    List<Stock> getAllStocksByIssuerName(String name);
}
