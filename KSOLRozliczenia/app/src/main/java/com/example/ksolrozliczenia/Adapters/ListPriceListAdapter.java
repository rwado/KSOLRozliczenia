package com.example.ksolrozliczenia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ksolrozliczenia.Model.PriceListItem;
import com.example.ksolrozliczenia.R;

import java.util.List;

public class ListPriceListAdapter extends BaseAdapter implements ListAdapter {

    private List<PriceListItem> itemList;
    private Context context;

    public ListPriceListAdapter(List<PriceListItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_price_list_adapter,null);
        }

        TextView textItemName = view.findViewById(R.id.textItemName);
        TextView textItemSize = view.findViewById(R.id.textItemSize);
        TextView textItemPrice = view.findViewById(R.id.textItemPrice);

        textItemName.setText(itemList.get(i).getDescription());
        textItemSize.setText(itemList.get(i).getSize());
        textItemPrice.setText(itemList.get(i).getPrice());

        return view;
    }
}