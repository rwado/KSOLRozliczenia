package com.example.ksolrozliczenia.Model;

public class ImageHandler {
    private String key;
    private String value;

    public ImageHandler(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public ImageHandler() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
