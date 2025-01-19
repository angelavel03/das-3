package com.analysis.stock.controller;

import com.analysis.stock.model.Stock;
import com.analysis.stock.service.MicroserviceClient;
import com.analysis.stock.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@CrossOrigin(origins="*")
@RequestMapping("/api")
public class HomeController {
    private final StockService stockService;
    private final MicroserviceClient microserviceClient;

    public HomeController(StockService stockService, MicroserviceClient microserviceClient) {
        this.stockService = stockService;
        this.microserviceClient = microserviceClient;
    }

    @GetMapping("/stocks/{name}")
    public ResponseEntity<List<Stock>> getStocks(@PathVariable String name) {
        List<Stock> stocks = stockService.getAllStocksByIssuerName(name);
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @GetMapping("/all/names")
    public ResponseEntity<List<String>> getAllIssuerNames() {
        return new ResponseEntity<>(
                microserviceClient.fetchIssuerNames("https://www.mse.mk/en/stats/symbolhistory/MPT"),
                HttpStatus.OK
        );
    }
}