package com.example.ksolrozliczenia.TrainingGroupsOrdersFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ksolrozliczenia.Adapters.ListPupilAdapter;
import com.example.ksolrozliczenia.Model.DataToPass;
import com.example.ksolrozliczenia.Model.Item;
import com.example.ksolrozliczenia.Model.Order;
import com.example.ksolrozliczenia.Model.Pupil;
import com.example.ksolrozliczenia.OrdersGivenFragments.OrdersGivenFragment2;
import com.example.ksolrozliczenia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class YourGroups extends Fragment {

    private DatabaseReference mGroupsReference = FirebaseDatabase.getInstance().getReference("Groups");

    private final String PASSWORD = "ZARZĄD KSOL";

    private ListView mListGroups;
    private Spinner mSpinnerChooseGroup;
    private Button mButtonNewGroup, mButtonNewPupil, mButtonDeleteGroup;


    private List<String> groupList = new ArrayList<>();
    private List<String> pupilNames = new ArrayList<>();
    private List<Pupil> pupilList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private ListPupilAdapter pupilAdapter;
    private DataToPass dataToPass = new DataToPass();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_groups, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            dataToPass = (DataToPass) bundle.getSerializable("DATA");
        } else {
            getActivity().getFragmentManager().popBackStack();
        }

        mListGroups = view.findViewById(R.id.listViewGroups);
        mSpinnerChooseGroup = view.findViewById(R.id.spinnerChooseGroup);
        mButtonNewGroup = view.findViewById(R.id.buttonNewGroup);
        mButtonNewPupil = view.findViewById(R.id.buttonNewPupil);
        mButtonDeleteGroup = view.findViewById(R.id.buttonDeleteGroup);

        pupilAdapter = new ListPupilAdapter(pupilNames, getContext());
        mListGroups.setAdapter(pupilAdapter);

        spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, groupList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerChooseGroup.setAdapter(spinnerAdapter);


        mListGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPupilDialog(i);

            }
        });

        mSpinnerChooseGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mGroupsReference.child(dataToPass.getMyName()).child(groupList.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pupilNames.clear();
                        pupilList.clear();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Pupil pupil = child.getValue(Pupil.class);
                            if(pupil != null) {
                                if(!pupil.getPupilName().equals("ghostpupil")) {
                                    pupilNames.add(pupil.getPupilName());
                                    pupilList.add(pupil);
                                }
                            }
                        }
                        Collections.sort(pupilNames);
                        Collections.sort(pupilList, new Comparator<Pupil>() {
                            @Override
                            public int compare(Pupil pupilOne, Pupil pupilTwo) {
                               return pupilOne.getPupilName().compareTo(pupilTwo.getPupilName());
                            }
                        });
                        pupilAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mButtonNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewGroupDialog();
            }
        });

        mButtonNewPupil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewPupilDialog();
            }
        });

        mButtonDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteSelectedGroupDialog();
            }
        });

        mGroupsReference.child(dataToPass.getMyName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    groupList.add(child.getKey());
                }
                spinnerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void showDeleteSelectedGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String message = "Czy napewno chcesz usunąć grupę:\n\"" + mSpinnerChooseGroup.getSelectedItem().toString() +"\"?" +
                "\nGrupa wraz z uczniami zostanie trwale usunięta";
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Potwierdź",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mGroupsReference.child(dataToPass.getMyName()).child(mSpinnerChooseGroup.getSelectedItem().toString()).removeValue();
                        spinnerAdapter.notifyDataSetChanged();
                        FragmentManager fm = getFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DATA", dataToPass);
                        YourGroups yourGroupsFragment = new YourGroups();
                        yourGroupsFragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.switch_fragment, yourGroupsFragment).commit();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                "Anuluj",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showNewPupilDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_new_pupil);

        TextView textGroupName = dialog.findViewById(R.id.textGroupName);
        String string = "Dodaj do grupy:\n" + mSpinnerChooseGroup.getSelectedItem().toString();
        textGroupName.setText(string);

        final EditText editNewPupilName = dialog.findViewById(R.id.editNewPupilName);
        Button buttonAccept = (Button) dialog.findViewById(R.id.buttonAcceptDialog);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancelDialog);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editNewPupilName.getText().toString().equals("")) {
                    String key = mGroupsReference.child(dataToPass.getMyName()).child(mSpinnerChooseGroup.getSelectedItem().toString()).push().getKey();
                    mGroupsReference.child(dataToPass.getMyName()).child(mSpinnerChooseGroup.getSelectedItem().toString()).child(key).setValue(new Pupil(editNewPupilName.getText().toString(), key));

                }
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (!((Activity) getActivity()).isFinishing()) {
            dialog.show();
        }

    }

    private void showNewGroupDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_new_group);

        final EditText editNewGroupName = dialog.findViewById(R.id.editNewGroupName);

        Button buttonAccept = (Button) dialog.findViewById(R.id.buttonAcceptDialog);
        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancelDialog);


        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editNewGroupName.getText().toString().equals("")) {
                    String key = mGroupsReference.child(dataToPass.getMyName()).child(editNewGroupName.getText().toString()).push().getKey();
                    mGroupsReference.child(dataToPass.getMyName()).child(editNewGroupName.getText().toString()).child(key).setValue(new Pupil("ghostpupil", key));
                    FragmentManager fm = getFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", dataToPass);
                    YourGroups yourGroupsFragment = new YourGroups();
                    yourGroupsFragment.setArguments(bundle);
                    fm.beginTransaction().replace(R.id.switch_fragment, yourGroupsFragment).commit();
                }

                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (!((Activity) getActivity()).isFinishing()) {
            dialog.show();
        }
    }

    private void showPupilDialog(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setCancelable(true);
        builder.setMessage(pupilNames.get(i));

        builder.setNeutralButton("ANULUJ",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(
                "ZAMÓW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FragmentManager fm = getFragmentManager();
                        OrdersGivenFragment2 fragment = new OrdersGivenFragment2();
                        Bundle bundle = new Bundle();
                        Item item = new Item("","", "", "", "", "", "");
                        Order order = new Order("", item, 0, "", "", "");
                        order.setSenderName(dataToPass.getMyName());
                        order.setRecipientName(pupilNames.get(i));
                        dataToPass.setOrder(order);
                        dataToPass.setFromWhichActivity(DataToPass.WhichActivity.ACTIVITY_TRAINING_GROUPS_ORDERS);
                        bundle.putSerializable("DATA", dataToPass);
                        fragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.switch_fragment, fragment).commit();
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton(
                "USUŃ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mGroupsReference.child(dataToPass.getMyName()).child(mSpinnerChooseGroup.getSelectedItem().toString()).child(pupilList.get(i).getPupilId()).removeValue();
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
