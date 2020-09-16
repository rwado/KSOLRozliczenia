package com.example.ksolrozliczenia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ksolrozliczenia.R;

import java.util.List;

public class ListPupilAdapter extends BaseAdapter implements ListAdapter {

    private List<String> pupilList;
    private Context context;

    public ListPupilAdapter(List<String> pupilList, Context context) {
        this.pupilList = pupilList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pupilList.size();
    }

    @Override
    public Object getItem(int i) {
        return pupilList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_groups_adapter,null);
        }

        TextView textName = view.findViewById(R.id.textPupilName);
        TextView textNumber = view.findViewById(R.id.textNumber);

        textName.setText(pupilList.get(i));
        String string = i+1 + ".";
        textNumber.setText(string);

        return view;
    }
}