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

public class ListArchivalPupilsAdapter extends BaseAdapter implements ListAdapter {

    private List<Order> orders;
    private Context context;

    public ListArchivalPupilsAdapter(List<Order> orders, Context context) {
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
            view = inflater.inflate(R.layout.list_orders_archival_pupils_layout,null);
        }

        TextView textDescription = view.findViewById(R.id.textDescription);
        TextView textQuantity = view.findViewById(R.id.textQuantity);
        TextView textRecipient = view.findViewById(R.id.textRecipient);
        TextView textDate = view.findViewById(R.id.textDate);

        textDescription.setText(orders.get(i).getItem().getDescription());
        textQuantity.setText(String.valueOf(orders.get(i).getQuantity()));
        textRecipient.setText(orders.get(i).getRecipientName());
        textDate.setText(orders.get(i).getDate());

        return view;
    }
}
