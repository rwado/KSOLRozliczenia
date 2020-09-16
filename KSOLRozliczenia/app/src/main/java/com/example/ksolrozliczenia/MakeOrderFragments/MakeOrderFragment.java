package com.example.ksolrozliczenia.MakeOrderFragments;

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
import com.example.ksolrozliczenia.OrdersGivenFragments.OrdersGivenFragment2;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MakeOrderFragment extends Fragment {

    private DatabaseReference mGroupsReference = FirebaseDatabase.getInstance().getReference("MyUser");

    private ListView mListUserList;
    private ListChooseUserAdapter userAdapter;

    private DataToPass dataToPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_order, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        } else {
            getActivity().getFragmentManager().popBackStack();
        }

        mListUserList = view.findViewById(R.id.listViewUserList);
        userAdapter = new ListChooseUserAdapter(dataToPass.getUserList(), getContext());
        mListUserList.setAdapter(userAdapter);

        mListUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment2 fragment = new OrdersGivenFragment2();
                Bundle bundle = new Bundle();
                Item item = new Item("","", "", "", "", "", "");
                Order order = new Order("", item, 0, dataToPass.getMyName(), dataToPass.getUserList().get(i).getName(), "");
                dataToPass.setOrder(order);
                dataToPass.setFromWhichActivity(DataToPass.WhichActivity.ACTIVITY_MAKE_ORDER);
                bundle.putSerializable("DATA", dataToPass);
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
            }
        });

        return view;
    }



}
