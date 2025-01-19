package com.analysis.stockmicroservice.controller;

import com.analysis.stockmicroservice.service.StockIssuerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/microservice")
@CrossOrigin(origins="*")
@Validated
public class StockIssuerController {
    private final StockIssuerService issuerService;

    public StockIssuerController(StockIssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @GetMapping("/issuers")
    public List<String> getIssuerNames() {
        return issuerService.getAllIssuerNames();
    }
}
