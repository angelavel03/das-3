package com.analysis.stock.service.impl;

import com.analysis.stock.model.Stock;
import com.analysis.stock.repository.StockRepository;
import com.analysis.stock.service.StockService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final int RSI_PERIOD = 14;
    private static final int SMA_PERIOD = 5;
    private static final int EMA_PERIOD = 10;
    private static final int WPR_PERIOD = 14;

    // Calculate RSI for a list of prices
    public double calculateRSI(List<Double> prices) {
        if (prices.size() < RSI_PERIOD) {
            return -1; // Not enough data
        }

        List<Double> gains = prices.stream()
                .map(i -> prices.get((int) (i - 1)) - i)
                .filter(d -> d > 0)
                .toList();
        List<Double> losses = prices.stream()
                .map(i -> Math.abs(prices.get((int) (i - 1)) - i))
                .filter(d -> d < 0)
                .toList();

        double avgGain = gains.stream().limit(RSI_PERIOD).mapToDouble(Double::doubleValue).average().orElse(0);
        double avgLoss = losses.stream().limit(RSI_PERIOD).mapToDouble(Double::doubleValue).average().orElse(0);

        double rs = avgGain / avgLoss;

        return 100 - (100 / (1 + rs));
    }

    // Calculate SMA
    public double calculateSMA(List<Double> prices) {
        if (prices.size() < SMA_PERIOD) {
            return -1;
        }
        return prices.stream().limit(SMA_PERIOD).mapToDouble(Double::doubleValue).average().orElse(0);
    }

    // Calculate EMA
    public double calculateEMA(List<Double> prices) {
        if (prices.size() < EMA_PERIOD) {
            return -1;
        }

        double multiplier = 2.0 / (EMA_PERIOD + 1);
        double ema = calculateSMA(prices); // Start with SMA for the first EMA value

        for (int i = EMA_PERIOD; i < prices.size(); i++) {
            ema = (prices.get(i) - ema) * multiplier + ema;
        }

        return ema;
    }

    // Calculate Williams Percent Range (WPR)
    public double calculateWPR(List<Double> highPrices, List<Double> lowPrices, Double closePrice) {
        if (highPrices.size() < WPR_PERIOD || lowPrices.size() < WPR_PERIOD) {
            return -1; // Not enough data
        }

        double highestHigh = highPrices.stream().limit(WPR_PERIOD).max(Double::compare).orElse(0.0);
        double lowestLow = lowPrices.stream().limit(WPR_PERIOD).min(Double::compare).orElse(0.0);

        return ((highestHigh - closePrice) / (highestHigh - lowestLow)) * -100;
    }

    // Signals: buy/sell based on calculated values
    public String getRSISignal(double rsi) {
        if (rsi < 30) {
            return "Buy";  // Oversold
        } else if (rsi > 70) {
            return "Sell";  // Overbought
        } else {
            return "Hold";
        }
    }

    public String getSMASignal(double sma, double currentPrice) {
        if (currentPrice > sma) {
            return "Buy";  // Price above SMA
        } else if (currentPrice < sma) {
            return "Sell";  // Price below SMA
        } else {
            return "Hold";
        }
    }

    public String getEMASignal(double ema, double currentPrice) {
        if (currentPrice > ema) {
            return "Buy";  // Price above EMA
        } else if (currentPrice < ema) {
            return "Sell";  // Price below EMA
        } else {
            return "Hold";
        }
    }

    public String getWPRSignal(double wpr) {
        if (wpr < -80) {
            return "Buy";  // Oversold
        } else if (wpr > -20) {
            return "Sell";  // Overbought
        } else {
            return "Hold";
        }
    }

    // Method to analyze a stock by its name and date range
    @Override
    public void analyzeStock(String stockName, String startDate, String endDate) {
        // Fetch stock data from repository based on stock name and date range
        List<Stock> stockData = stockRepository.findByNameAndDateBetween(stockName,
                LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Extract price data from the stock records
        List<Double> prices = stockData.stream().map(Stock::getLastTradePrice).collect(Collectors.toList());
        List<Double> highPrices = stockData.stream().map(Stock::getMax).collect(Collectors.toList());
        List<Double> lowPrices = stockData.stream().map(Stock::getMin).collect(Collectors.toList());
        Double closePrice = stockData.get(stockData.size() - 1).getLastTradePrice();

        // Calculate indicators
        double rsi = calculateRSI(prices);
        double sma = calculateSMA(prices);
        double ema = calculateEMA(prices);
        double wpr = calculateWPR(highPrices, lowPrices, closePrice);

        // Get signals for each indicator
        String rsiSignal = getRSISignal(rsi);
        String smaSignal = getSMASignal(sma, closePrice);
        String emaSignal = getEMASignal(ema, closePrice);
        String wprSignal = getWPRSignal(wpr);

        // Print results
        System.out.println("RSI: " + rsi + " Signal: " + rsiSignal);
        System.out.println("SMA: " + sma + " Signal: " + smaSignal);
        System.out.println("EMA: " + ema + " Signal: " + emaSignal);
        System.out.println("WPR: " + wpr + " Signal: " + wprSignal);
    }
}
