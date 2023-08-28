package com.bit.shoppingcart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private long lastBackPressedTime = 0;
    private static final long DOUBLE_TAP_EXIT_INTERVAL = 2000; // 2 seconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.list_list, container, false);
        RecyclerView listRecycler = rootview.findViewById(R.id.list_recyclerview);
        RelativeLayout emptyHandler = rootview.findViewById(R.id.empty_handler);
        FloatingActionButton addListBtn = rootview.findViewById(R.id.add_list_btn);
        listRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ArrayList<List> listList = new ArrayList<>();
        ListAdapter listAdapter = new ListAdapter(getContext(), listList);
        listRecycler.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        listAdapter.setOnItemClickListener((listId, listName) -> {
            Bundle args = new Bundle();
            args.putInt("listId", listId);
            args.putString("listName", listName);
            ItemFragment itemFragment = new ItemFragment();
            itemFragment.setArguments(args);
            requireFragmentManager().beginTransaction().replace(R.id.frame, itemFragment).commit();
        });
        MainActivity.listViewModel.getAllLists().observe(getViewLifecycleOwner(), new Observer<java.util.List<List>>() {
            @Override
            public void onChanged(java.util.List<List> lists) {
                listAdapter.setLists(lists);
                if(lists.size() < 1){
                    emptyHandler.setVisibility(View.VISIBLE);
                }else{
                    emptyHandler.setVisibility(View.GONE);
                }
            }
        });
        MainActivity.listViewModel.getListCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer listCount) {
                MainActivity.headerText.setText("All lists"+" "+"("+listCount+")");
            }
        });
        
        addListBtn.setOnClickListener(v -> {
            showCreateListDialog();
        });
        addListBtn.setOnLongClickListener(v -> {
            startQRCodeScanner();
            return true;
        });

        // Set up the double tap back press callback
        OnBackPressedCallback doubleTapExitCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastBackPressedTime < DOUBLE_TAP_EXIT_INTERVAL) {
                    requireActivity().finish(); // Exit the app
                } else {
                    lastBackPressedTime = currentTime;
                    Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Add the callback to the back press dispatcher
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), doubleTapExitCallback);

        return rootview;
    }
    public void startQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan QR Code");
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }
    private void showCreateListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_list_dialog, null);
        builder.setView(dialogView);

        EditText listNameInput = dialogView.findViewById(R.id.list_name_input);
        Button createListBtn = dialogView.findViewById(R.id.create_list_btn);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);

        createListBtn.setOnClickListener(V -> {
            if (listNameInput.getText().toString().trim().isEmpty()) {
                listNameInput.setError("Required");
            } else {
                String newListName = listNameInput.getText().toString().trim();
                MainActivity.listViewModel.getAllLists().observe(getViewLifecycleOwner(), lists -> {
                    boolean listNameExists = false;
                    for (List list : lists) {
                        if (list.getListName().equalsIgnoreCase(newListName)) {
                            listNameExists = true;
                            break;
                        }
                    }
                    if (listNameExists) {
                        listNameInput.setError("List name already exists");
                    } else {
                        MainActivity.listViewModel.insertList(new List(newListName));
                        Toast.makeText(getContext(), "List created", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

}
