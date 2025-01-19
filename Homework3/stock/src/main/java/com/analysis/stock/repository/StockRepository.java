package com.analysis.stock.repository;

import com.analysis.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByName(String name);
    List<Stock> findByNameAndDateBetween(String stockName, LocalDate startDate, LocalDate endDate);
}