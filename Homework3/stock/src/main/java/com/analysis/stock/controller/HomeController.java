package com.analysis.stock.controller;

import com.analysis.stock.model.Stock;
import com.analysis.stock.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@CrossOrigin(origins="*")
public class HomeController {
    private final StockService stockService;

    public HomeController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/api/stocks/{name}")
    public ResponseEntity<List<Stock>> getStocks(@PathVariable String name) {
        List<Stock> stocks = stockService.getAllStocksByIssuerName(name);
        stockService.analyzeStock(name, "2024-01-01", "2024-12-29");
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }
}