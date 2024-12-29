package com.analysis.stock.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
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
    private int volume;
    @Column(name = "best_turnover")
    private int bestTurnover;
    @Column(name = "total_turnover")
    private int totalTurnover;
}