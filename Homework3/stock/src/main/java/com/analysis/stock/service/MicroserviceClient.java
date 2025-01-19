package com.analysis.stock.service;

import java.util.*;

public interface MicroserviceClient {
    List<String> fetchIssuerNames(String url);
}
