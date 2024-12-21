package com.example.meli_coupon_api.service;

import com.example.meli_coupon_api.model.CouponResponse;
import com.example.meli_coupon_api.model.Item;
import com.example.meli_coupon_api.restclient.ItemClient;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

  @Autowired private ItemClient itemClient;
  private final Map<String, Float> priceCache = new HashMap<>();

  public CouponResponse calculateOptimalItems(Set<String> itemIds, Float amount) {
    Map<String, Float> items = new HashMap<>();
    for (String id : itemIds) {
      Float price = getItemPrice(id);
      if (price != null) {
        items.put(id, price);
      }
    }

    Set<String> selectedItems = calculate(items, amount);
    if (selectedItems.isEmpty()) {
      return null;
    }

    float total = 0;
    for (String id : selectedItems) {
      total += items.get(id);
    }

    return new CouponResponse(selectedItems, total);
  }

  private Float getItemPrice(String itemId) {
    if (priceCache.containsKey(itemId)) {
      return priceCache.get(itemId);
    }

    Float price = itemClient.getItemPrice(itemId);
    if (price != null) {
      priceCache.put(itemId, price);
    }
    return price;
  }

  public Set<String> calculate(Map<String, Float> items, Float amount) {
    // Convertir el Map a un Array para poder acceder por índice
    Item[] itemArray = items.entrySet().stream()
            .map(entry -> new Item(entry.getKey(), entry.getValue()))
            .toArray(Item[]::new);

    int n = itemArray.length;
    int W = Math.round(amount * 100); // Convertir a centavos para manejar enteros
    int[] prices = new int[n];

    // Convertir precios a centavos
    for (int i = 0; i < n; i++) {
      prices[i] = Math.round(itemArray[i].getPrice() * 100);
    }

    // Matriz de programación dinámica
    boolean[][] dp = new boolean[n + 1][W + 1];
    dp[0][0] = true;

    // Llenar la matriz dp
    for (int i = 1; i <= n; i++) {
      for (int j = 0; j <= W; j++) {
        if (j >= prices[i - 1]) {
          dp[i][j] = dp[i - 1][j] || dp[i - 1][j - prices[i - 1]];
        } else {
          dp[i][j] = dp[i - 1][j];
        }
      }
    }

    // Encontrar el valor máximo posible
    int max = 0;
    for (int j = W; j >= 0; j--) {
      if (dp[n][j]) {
        max = j;
        break;
      }
    }

    // Reconstruir la solución
    Set<String> selectedItems = new LinkedHashSet<>();
    int remaining = max;
    for (int i = n; i > 0 && remaining > 0; i--) {
      if (!dp[i - 1][remaining]) {
        selectedItems.add(itemArray[i - 1].getId());
        remaining -= prices[i - 1];
      }
    }
    return selectedItems;
  }
}
