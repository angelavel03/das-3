package com.analysis.stock.controller;

import com.analysis.stock.model.Stock;
import com.analysis.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }
}