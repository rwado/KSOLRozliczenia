package com.example.ksolrozliczenia.Model;

import androidx.annotation.NonNull;

public class PriceListItem {
    String description;
    String size;
    String price;

    public PriceListItem() {

    }

    public PriceListItem(String description, String size, String price) {
        this.description = description;
        this.size = size;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return description + " " + size + " " + price;
    }
}