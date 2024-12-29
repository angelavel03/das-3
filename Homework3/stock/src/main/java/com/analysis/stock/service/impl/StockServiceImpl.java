package com.analysis.stock.service.impl;

import com.analysis.stock.model.Stock;
import com.analysis.stock.repository.StockRepository;
import com.analysis.stock.service.StockService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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

    @Override
    public void generateAnalysisByIssuerName(String name) {
        String pythonScriptPath = "/src/main/python/tech_indicators.py"; // Update with your Python script's path

        try {
            // Construct the command to execute Python script
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, name);

            // Start the process
            Process process = processBuilder.start();

            // Capture the output of the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to complete and check the exit status
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.out.println("Python script execution failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
