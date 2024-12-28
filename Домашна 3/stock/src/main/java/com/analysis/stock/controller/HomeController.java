package com.analysis.stock.controller;

import com.analysis.stock.model.Stock;
import com.analysis.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/stocks")
public class HomeController {
    private final StockService stockService;

    public HomeController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/api")
    public List<Stock> getStocks() {
        return stockService.getAllStocks();
    }
}
