package com.example.ksolrozliczenia.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ksolrozliczenia.MainActivity;
import com.example.ksolrozliczenia.Model.Category;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class ListChooseCategoryAdapter extends BaseAdapter implements ListAdapter {

    DatabaseReference mArchivalReference = FirebaseDatabase.getInstance().getReference("ArchivalOrdersPupils");

    private List<Category> categoryList;
    private Context context;

    public ListChooseCategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_choose_category_adapter, null);
        }

        TextView textCategoryName = view.findViewById(R.id.textCategoryNameNewOrderList);
        ImageView imageCategoryItem = view.findViewById(R.id.imageCategoryNewOrderList);

        textCategoryName.setText(categoryList.get(i).getName());

        int resourceId = R.drawable.oyama_logo;
        HashMap<String, String> imageMap = MainActivity.imageMap;
        if (imageMap.get(categoryList.get(i).getName()) != null) {
            resourceId = context.getResources().getIdentifier(imageMap.get(categoryList.get(i).getName()), "drawable", context.getPackageName());
        }
        imageCategoryItem.setImageResource(resourceId);

        return view;
    }
}