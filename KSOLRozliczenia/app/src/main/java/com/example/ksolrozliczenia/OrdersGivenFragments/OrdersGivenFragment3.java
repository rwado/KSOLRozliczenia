package com.example.ksolrozliczenia.OrdersGivenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ksolrozliczenia.Adapters.ListChooseCategoryAdapter;
import com.example.ksolrozliczenia.Model.Category;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OrdersGivenFragment3 extends Fragment {


    private ListView mListView;
    private Button mButtonBack;

    private List<Category> subcategoryList = new ArrayList<>();

    private DataToPass dataToPass = new DataToPass();
    private DatabaseReference mSubcategoriesReference = FirebaseDatabase.getInstance().getReference("PriceList");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order_3, container, false);


        mButtonBack = view.findViewById(R.id.buttonBackNewOrderFragment3);
        mListView = view.findViewById(R.id.listSubcategoryNewOrder);
        final ListChooseCategoryAdapter subcategoryAdapter = new ListChooseCategoryAdapter(subcategoryList, getContext());
        mListView.setAdapter(subcategoryAdapter);
        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        }
        mSubcategoriesReference = mSubcategoriesReference.child(dataToPass.getOrder().getItem().getCategoryName());
        mSubcategoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subcategoryList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    subcategoryList.add(new Category("", child.getKey()));

                }
                subcategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        subcategoryAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataToPass.getOrder().getItem().setSubcategoryName(subcategoryList.get(position).getName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment4 fragment4 = new OrdersGivenFragment4();
                fragment4.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment4).commit();
            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment2 fragment2 = new OrdersGivenFragment2();
                fragment2.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment2).commit();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
