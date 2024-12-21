package com.example.meli_coupon_api.controller;

import com.example.meli_coupon_api.model.CouponRequest;
import com.example.meli_coupon_api.model.CouponResponse;
import com.example.meli_coupon_api.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCouponItems_WhenValidRequest_ShouldReturnOk() throws Exception {
        // Arrange
        Set<String> itemIds = new LinkedHashSet<>();
        itemIds.add("MLA1");
        itemIds.add("MLA2");

        CouponRequest request = new CouponRequest();
        request.setItem_ids(itemIds);
        request.setAmount(500f);

        Set<String> responseItemIds = new LinkedHashSet<>();
        responseItemIds.add("MLA1");
        CouponResponse response = new CouponResponse(responseItemIds, 100f);

        when(couponService.calculateOptimalItems(any(), any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/coupon/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_ids").isArray())
                .andExpect(jsonPath("$.total").value(100f));
    }

    @Test
    void getCouponItems_WhenNoItemsFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        Set<String> itemIds = new LinkedHashSet<>();
        itemIds.add("MLA1");
        itemIds.add("MLA2");

        CouponRequest request = new CouponRequest();
        request.setItem_ids(itemIds);
        request.setAmount(500f);

        when(couponService.calculateOptimalItems(any(), any())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/coupon/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCouponItems_WhenInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String invalidRequest = "{\"item_ids\": null, \"amount\": null}";

        // Act & Assert
        mockMvc.perform(post("/coupon/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCouponItems_WhenEmptyItemIds_ShouldReturnNotFound() throws Exception {
        // Arrange
        CouponRequest request = new CouponRequest();
        request.setItem_ids(new LinkedHashSet<>());
        request.setAmount(500f);

        when(couponService.calculateOptimalItems(any(), any())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/coupon/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
