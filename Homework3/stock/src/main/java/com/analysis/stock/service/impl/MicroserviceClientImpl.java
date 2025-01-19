package com.analysis.stock.service.impl;

import com.analysis.stock.service.MicroserviceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MicroserviceClientImpl implements MicroserviceClient {
    private final RestTemplate restTemplate;

    @Value("${microservice.url}")
    private String microserviceUrl;

    public MicroserviceClientImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }
    @Override
    public List<String> fetchIssuerNames(String url) {
        return restTemplate.getForObject(microserviceUrl + "/microservice/issuers", List.class);
    }
}
