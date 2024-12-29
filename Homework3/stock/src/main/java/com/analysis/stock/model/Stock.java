package com.analysis.stock.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "last_trade_price")
    private Double lastTradePrice;
    @Column(name = "max")
    private Double max;
    @Column(name = "min")
    private Double min;
    @Column(name = "avg_price")
    private Double avgPrice;
    @Column(name = "cfg")
    private Double cfg;
    @Column(name = "volume")
    private Integer volume;
    @Column(name = "best_turnover")
    private Integer bestTurnover;
    @Column(name = "total_turnover")
    private Integer totalTurnover;

    public Stock() {}

    public Stock(String name, LocalDate date, Double lastTradePrice, Double max, Double min, Double avgPrice,
                 Double cfg, Integer volume, Integer bestTurnover, Integer totalTurnover) {
        this.name = name;
        this.date = date;
        this.lastTradePrice = lastTradePrice;
        this.max = max;
        this.min = min;
        this.avgPrice = avgPrice;
        this.cfg = cfg;
        this.volume = volume;
        this.bestTurnover = bestTurnover;
        this.totalTurnover = totalTurnover;
    }

    public static Stock create(JsonNode json) {
        String name = json.get("issuer").asText();
        String date = json.get("date").asText();
        Double lastTradePrice = json.get("lastTradePrice").asDouble();
        Double min = json.get("minPrice").asDouble();
        Double max = json.get("maxPrice").asDouble();
        Double avgPrice = json.get("avgPrice").asDouble();
        Double cfg = json.get("percentChange").asDouble();
        Integer volume = json.get("volume").asInt();
        Integer bestTurnover = json.get("turnoverBest").asInt();
        Integer totalTurnover = json.get("totalTurnover").asInt();

        return new Stock(name, LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                lastTradePrice, min, max, avgPrice, cfg, volume, bestTurnover, totalTurnover);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getLastTradePrice() {
        return lastTradePrice;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }

    public Double getAvgPrice() {
        return avgPrice;
    }

    public Double getCfg() {
        return cfg;
    }

    public Integer getVolume() {
        return volume;
    }

    public Integer getBestTurnover() {
        return bestTurnover;
    }

    public Integer getTotalTurnover() {
        return totalTurnover;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", lastTradePrice=" + lastTradePrice +
                ", max=" + max +
                ", min=" + min +
                ", avgPrice=" + avgPrice +
                ", cfg=" + cfg +
                ", volume=" + volume +
                ", bestTurnover=" + bestTurnover +
                ", totalTurnover=" + totalTurnover +
                '}';
    }
}