package com.example.ksolrozliczenia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.R;

import java.util.List;

public class ListOrdersReceived extends BaseAdapter implements ListAdapter {

    private List<Order> orders;
    private Context context;

    public ListOrdersReceived(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_orders_given_layout,null);
        }

        TextView textDescription = view.findViewById(R.id.textDescriptionOrdersGiven);
        TextView textQuantity = view.findViewById(R.id.textQuantityOrdersGiven);
        TextView textPriceSum = view.findViewById(R.id.textPriceSumOrdersGiven);
        TextView textSender = view.findViewById(R.id.textRecipientOrdersGiven);
        TextView textDate = view.findViewById(R.id.textDateOrdersGiven);

        textDescription.setText(orders.get(i).getItem().getDescription());
        textQuantity.setText(String.valueOf(orders.get(i).getQuantity()));
        int quantity = orders.get(i).getQuantity();
        int price = Integer.parseInt(orders.get(i).getItem().getPrice());
        int priceSum = quantity * price;
        textPriceSum.setText(String.valueOf(priceSum));
        textSender.setText(orders.get(i).getSenderName());
        textDate.setText(orders.get(i).getDate());

        return view;
    }
}
