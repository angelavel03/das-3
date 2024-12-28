package com.analysis.stock.service.impl;

import com.analysis.stock.model.Stock;
import com.analysis.stock.service.StockService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Override
    public List<Stock> getAllStocks() {
        return new ArrayList<>();
    }
}
