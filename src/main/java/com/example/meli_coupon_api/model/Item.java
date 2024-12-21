package com.example.meli_coupon_api.model;

public class Item {
    private String id;
    private float price;

    public Item(String id, float price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

}
