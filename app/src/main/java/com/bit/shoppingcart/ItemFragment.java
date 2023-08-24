package com.bit.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.item_list, container, false);
        int listId = getArguments().getInt("listId", -1);
        RecyclerView itemRecycler = rootview.findViewById(R.id.item_recyclerview);
        itemRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Item> itemList = new ArrayList<>();
        ItemAdapter itemAdapter = new ItemAdapter(getContext(), itemList);
        itemRecycler.setAdapter(itemAdapter);
        MainActivity.itemViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<java.util.List<Item>>() {
            @Override
            public void onChanged(java.util.List<Item> items) {
                itemAdapter.setItems(items);
            }
        });
        MainActivity.itemViewModel.getListItems(listId).observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemAdapter.setItems(items);
            }
        });
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate to FragmentB
                ListFragment listFragment = new ListFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, listFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        return rootview;
    }
}
