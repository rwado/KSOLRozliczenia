package com.example.ksolrozliczenia.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataToPass implements Serializable {

    private WhichActivity fromWhichActivity;
    private Order order;
    private List<User> userList = new ArrayList<>();
    private String myName;

    public DataToPass() {}

    public DataToPass(WhichActivity fromWhichActivity, Order order, List<User> userList, String myName) {
        this.fromWhichActivity = fromWhichActivity;
        this.order = order;
        this.userList = userList;
        this.myName = myName;
    }

    public WhichActivity getFromWhichActivity() {
        return fromWhichActivity;
    }

    public void setFromWhichActivity(WhichActivity fromWhichActivity) {
        this.fromWhichActivity = fromWhichActivity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public enum WhichActivity {
        ACTIVITY_ORDERS_GIVEN,
        ACTIVITY_MAKE_ORDER,
        ACTIVITY_TRAINING_GROUPS_ORDERS
    }
}
