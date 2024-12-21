package com.example.meli_coupon_api.service;

import com.example.meli_coupon_api.model.CouponResponse;
import com.example.meli_coupon_api.restclient.ItemClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private CouponService couponService;

    private Set<String> itemIds;

    @BeforeEach
    void setUp() {
        itemIds = new LinkedHashSet<>();
        itemIds.add("MLA1");
        itemIds.add("MLA2");
        itemIds.add("MLA3");
        itemIds.add("MLA4");
        itemIds.add("MLA5");
    }

    @Test
    void calculateOptimalItems_WhenValidInputs_ShouldReturnOptimalItems() {
        // Arrange
        when(itemClient.getItemPrice("MLA1")).thenReturn(100f);
        when(itemClient.getItemPrice("MLA2")).thenReturn(210f);
        when(itemClient.getItemPrice("MLA3")).thenReturn(220f);
        when(itemClient.getItemPrice("MLA4")).thenReturn(80f);
        when(itemClient.getItemPrice("MLA5")).thenReturn(90f);

        // Act
        CouponResponse response = couponService.calculateOptimalItems(itemIds, 500f);

        // Assert
        assertNotNull(response);
        assertEquals(4, response.getItem_ids().size());
        assertTrue(response.getTotal() <= 500f);
        assertTrue(response.getItem_ids().contains("MLA1"));
        assertTrue(response.getItem_ids().contains("MLA3"));
    }

    @Test
    void calculateOptimalItems_WhenInsufficientAmount_ShouldReturnNull() {
        // Arrange
        when(itemClient.getItemPrice("MLA1")).thenReturn(100f);
        when(itemClient.getItemPrice("MLA2")).thenReturn(210f);
        when(itemClient.getItemPrice("MLA3")).thenReturn(220f);

        // Act
        CouponResponse response = couponService.calculateOptimalItems(itemIds, 50f);

        // Assert
        assertNull(response);
    }

    @Test
    void calculateOptimalItems_WhenEmptyItemIds_ShouldReturnNull() {
        // Arrange
        Set<String> emptySet = new LinkedHashSet<>();

        // Act
        CouponResponse response = couponService.calculateOptimalItems(emptySet, 500f);

        // Assert
        assertNull(response);
    }

    @Test
    void calculateOptimalItems_WhenSomeItemsNotFound_ShouldCalculateWithAvailableItems() {
        // Arrange
        when(itemClient.getItemPrice("MLA1")).thenReturn(100f);
        when(itemClient.getItemPrice("MLA2")).thenReturn(null);
        when(itemClient.getItemPrice("MLA3")).thenReturn(220f);

        // Act
        CouponResponse response = couponService.calculateOptimalItems(itemIds, 500f);

        // Assert
        assertNotNull(response);
        assertTrue(response.getTotal() <= 500f);
        assertFalse(response.getItem_ids().contains("MLA2"));
    }

    @Test
    void calculateOptimalItems_ShouldUseCachedPrices() {
        // Arrange
        when(itemClient.getItemPrice("MLA1")).thenReturn(100f);

        // Act
        couponService.calculateOptimalItems(Set.of("MLA1"), 500f);
        couponService.calculateOptimalItems(Set.of("MLA1"), 500f);

        // Assert
        verify(itemClient, times(1)).getItemPrice("MLA1");
    }
}
