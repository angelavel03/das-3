package com.analysis.stock.model;

import java.time.LocalDate;

public class Stock {
    private LocalDate date;
    private Double lastTradePrice;
    private Double max;
    private Double min;
    private Double avgPrice;
    private Double cfg;
    private int volume;
    private int bestTurnover;
    private int totalTurnover;
}
