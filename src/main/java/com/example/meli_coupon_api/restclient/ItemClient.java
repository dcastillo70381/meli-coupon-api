package com.example.meli_coupon_api.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ItemClient {
    private final RestTemplate restTemplate;
    private final String itemApiUrl;

    public ItemClient(@Value("${mercadolibre.api.url}") String itemApiUrl) {
        this.restTemplate = new RestTemplate();
        this.itemApiUrl = itemApiUrl;
    }
    public Float getItemPrice(String itemId) {
        String url = itemApiUrl + "/items/" + itemId;
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("price")) {
                return ((Number) response.get("price")).floatValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
