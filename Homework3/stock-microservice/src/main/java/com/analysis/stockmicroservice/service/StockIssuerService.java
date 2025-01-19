package com.analysis.stockmicroservice.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class StockIssuerService {
    public List<String> getAllIssuerNames() {
        List<String> names = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://www.mse.mk/en/stats/symbolhistory/MPT/").get();
            Elements rows = doc.select("#Code > option");
            for (Element row : rows) {
                if (!issuerContainsDigit(row.text())) {
                    names.add(row.text());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return names;
    }

    private boolean issuerContainsDigit(String issuerName) {
        return issuerName.chars().anyMatch(Character::isDigit);
    }
}
