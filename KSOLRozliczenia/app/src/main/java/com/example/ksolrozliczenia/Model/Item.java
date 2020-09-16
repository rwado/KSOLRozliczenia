package com.example.ksolrozliczenia.Model;


public class Item {

    String itemId;
    String categoryName;
    String subcategoryName;
    String description;
    String size;
    String color;
    String price;

    public Item() {

    }

    public Item(String itemId, String categoryName, String subcategoryName, String description, String size, String color, String price) {
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.subcategoryName = subcategoryName;
        this.description = description;
        this.size = size;
        this.color = color;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return description + '\'' +
                ", Rozmiar='" + size + '\'' +
                ", Kolor='" + color + '\'' +
                ", Cena='" + price + '\'' +
                '}';
    }
}
