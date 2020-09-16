package com.example.ksolrozliczenia.OrdersGivenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ksolrozliczenia.Adapters.ListChooseUserAdapter;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Item;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OrdersGivenFragment1 extends Fragment {


    private ListView mListView;

    private DataToPass dataToPass = new DataToPass();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order_1, container, false);

        Bundle bundle = getArguments();
        dataToPass = (DataToPass) bundle.getSerializable("DATA");


        mListView = view.findViewById(R.id.listRecipientNewOrder);
        ListChooseUserAdapter userAdapter = new ListChooseUserAdapter(dataToPass.getUserList(), getContext());
        mListView.setAdapter(userAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!dataToPass.getMyName().equals("")) {
                    Item item = new Item("","", "", "", "", "", "");
                    Order order = new Order("", item, 0, dataToPass.getMyName(), dataToPass.getUserList().get(position).getName(), "");
                    dataToPass.setOrder(order);
                    FragmentManager fm = getFragmentManager();
                    OrdersGivenFragment2 fragment = new OrdersGivenFragment2();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", dataToPass);
                    fragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
                }

            }
        });





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }





}
