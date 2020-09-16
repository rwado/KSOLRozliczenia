package com.example.ksolrozliczenia.Model;

import java.io.Serializable;

public class Order implements Serializable {

    private String orderId;
    private Item item;
    private int quantity;
    private String senderName;
    private String recipientName;
    private String date;

    public Order() {

    }

    public Order(String orderId, Item item, int quantity, String senderName, String recipientName, String date) {
        this.orderId = orderId;
        this.item = item;
        this.quantity = quantity;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String string;

        string = recipientName +
                "\n" + item.getSubcategoryName();

        if(!item.getSize().equals("")) {
            string += "\nRozmiar: " + item.getSize();
        }
        if(!item.getColor().equals("")) {
            string += "\nKolor: " + item.getColor();
        }
        string += "\nIlość: " + quantity;
        int price = quantity * Integer.parseInt(item.getPrice());
        string += "\nCena łącznie: " + price + " PLN";



        return  string;
    }
}
