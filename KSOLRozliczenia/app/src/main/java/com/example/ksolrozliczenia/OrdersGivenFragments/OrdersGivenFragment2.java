package com.example.ksolrozliczenia.OrdersGivenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ksolrozliczenia.Adapters.ListChooseCategoryAdapter;
import com.example.ksolrozliczenia.MakeOrderFragments.MakeOrderFragment;
import com.example.ksolrozliczenia.Model.Category;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.R;
import com.example.ksolrozliczenia.TrainingGroupsOrdersFragments.YourGroups;
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

public class OrdersGivenFragment2 extends Fragment {


    private ListView mListView;
    private Button mButtonBack;

    private List<Category> categoryList = new ArrayList<>();
    private DataToPass dataToPass = new DataToPass();
    DatabaseReference mCategoriesReference = FirebaseDatabase.getInstance().getReference("Categories");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order_2, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        }

        mButtonBack = view.findViewById(R.id.buttonBackNewOrderFragment2);
        mListView = view.findViewById(R.id.listRecipientNewOrder);
        final ListChooseCategoryAdapter categoryAdapter = new ListChooseCategoryAdapter(categoryList, getContext());
        mListView.setAdapter(categoryAdapter);

        mCategoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Category category = child.getValue(Category.class);
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataToPass.getOrder().getItem().setCategoryName(categoryList.get(position).getName());
                FragmentManager fm = getFragmentManager();
                OrdersGivenFragment3 fragment = new OrdersGivenFragment3();
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", dataToPass);
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();

            }
        });

        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                if(dataToPass.getFromWhichActivity().equals(DataToPass.WhichActivity.ACTIVITY_ORDERS_GIVEN)){
                    OrdersGivenFragment1 fragment = new OrdersGivenFragment1();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", dataToPass);
                    fragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
                } else if(dataToPass.getFromWhichActivity().equals(DataToPass.WhichActivity.ACTIVITY_TRAINING_GROUPS_ORDERS)) {
                    YourGroups fragment = new YourGroups();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", dataToPass);
                    fragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
                } else if (dataToPass.getFromWhichActivity().equals(DataToPass.WhichActivity.ACTIVITY_MAKE_ORDER)) {
                    MakeOrderFragment fragment = new MakeOrderFragment();
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
