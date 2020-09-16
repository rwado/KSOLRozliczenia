package com.example.ksolrozliczenia.ArchivalOrdersFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ksolrozliczenia.Adapters.ListArchivalPupilsAdapter;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ArchivalPupilsFragment extends Fragment {

    private DatabaseReference mArchivalReference = FirebaseDatabase.getInstance().getReference("ArchivalOrdersPupils");
    private Button mButtonMonthPicker;
    private ListView mListArchival;

    private ListArchivalPupilsAdapter mListAdapter;
    private Calendar today = Calendar.getInstance();
    private List<Order> orderList = new ArrayList<>();
    private DataToPass dataToPass;
    private int year, month;
    private String yearString, monthString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archival_pupils, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        }

        mButtonMonthPicker = view.findViewById(R.id.buttonDatePickerTrainers);
        mListArchival = view.findViewById(R.id.listArchivalTrainers);

        mListAdapter = new ListArchivalPupilsAdapter(orderList, getContext());
        mListArchival.setAdapter(mListAdapter);

        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        yearString = String.valueOf(year);
        monthString = getMonthStringFromInt(month);

        String currentMonthYear = getMonthNameFromInt(month) + " " + year;
        mButtonMonthPicker.setText(currentMonthYear);


        mListArchival.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showInfoDialog(orderList.get(i));
            }
        });

        mButtonMonthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonthYearPicker();

            }
        });


        mArchivalReference.child(dataToPass.getMyName()).child(String.valueOf(yearString)).child(String.valueOf(monthString)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    orderList.add(order);

                }
                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void showInfoDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Informacje");

        builder.setMessage(getOrderInfoString(order));

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private String getOrderInfoString(Order order) {

        String size = order.getItem().getSize();
        if (!size.equals("")) {
            size = "\nRozmiar: " + size;
        }
        String color = order.getItem().getColor();
        if (!color.equals("")) {
            color = "\nKolor: " + color;
        }
        String price = String.valueOf(Integer.parseInt(order.getItem().getPrice()) * order.getQuantity());

        String string = "\nTowar wydał: " + order.getSenderName()
                + "\nTowar otrzymał: " + order.getRecipientName()
                + "\n\n" + order.getItem().getDescription()
                + size
                + color
                + "\nSztuk: " + order.getQuantity()
                + "\nCena łącznie: " + price + " PLN";

        return string;
    }

    private String getMonthStringFromInt(int month) {
        if (month < 10) {
            String string = "0" + String.valueOf(month + 1);
            return string;
        } else {
            return String.valueOf(month + 1);
        }
    }

    private void showMonthYearPicker() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {

                String month_name = getMonthNameFromInt(selectedMonth);
                String string = month_name + " " + String.valueOf(selectedYear);
                year = selectedYear;
                month = selectedMonth;
                yearString = String.valueOf(year);
                monthString = getMonthStringFromInt(month);

                mButtonMonthPicker.setText(string);

                mArchivalReference.child(dataToPass.getMyName()).child(String.valueOf(yearString)).child(String.valueOf(monthString)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orderList.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Order order = child.getValue(Order.class);
                            if (order.getSenderName().equals(dataToPass.getMyName()) || order.getRecipientName().equals(dataToPass.getMyName())) {
                                orderList.add(order);
                            }
                        }
                        mListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(month)
                .setMinYear(2020)
                .setActivatedYear(year)
                .setMaxYear(2080)
                .setTitle("Wybierz miesiąc i rok")
                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {

                    }
                })
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {

                    }
                })
                .build()
                .show();
    }

    private String getMonthNameFromInt(int month) {
        String[] monthName = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec",
                "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};
        return monthName[month];
    }
}
