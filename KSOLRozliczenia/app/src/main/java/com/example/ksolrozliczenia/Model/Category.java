package com.example.ksolrozliczenia.Model;

import androidx.annotation.NonNull;

public class Category {

    String categoryId;
    String name;

    public Category(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public Category() {

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
