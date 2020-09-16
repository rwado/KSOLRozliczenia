package com.example.ksolrozliczenia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ksolrozliczenia.Model.User;
import com.example.ksolrozliczenia.R;

import java.util.List;

public class ListChooseUserAdapter extends BaseAdapter implements ListAdapter {

    private List<User> userList;
    private Context context;

    public ListChooseUserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_choose_user_adapter,null);
        }

        TextView textRecipientName = view.findViewById(R.id.textRecipientNameNewOrder);
        ImageView imageRecipientImage = view.findViewById(R.id.imageRecipientNewOrder);

        textRecipientName.setText(userList.get(i).getName());

        int resourceId;

        switch(userList.get(i).getName()){
            case "Ryszard Wadowski":
                resourceId = R.drawable.ryszard_wadowski_small;
                break;
            case "Marcin Wójcik":
                resourceId = R.drawable.marcin_wojcik_small;
                break;
            case "Paweł Lecki":
                resourceId = R.drawable.pawel_lecki_small;
                break;
            case "Mateusz Pichur":
                resourceId = R.drawable.mateusz_pichur_small;
                break;
            default:
                resourceId = R.drawable.person;
        }
        imageRecipientImage.setImageResource(resourceId);

        return view;
    }
}