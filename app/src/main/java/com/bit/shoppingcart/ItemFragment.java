package com.bit.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {
    private CardView addItemBtn;
    public static String listName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.item_list, container, false);
        addItemBtn = rootview.findViewById(R.id.add_item_btn);
        int listId = getArguments().getInt("listId", -1);
        listName = getArguments().getString("listName", "All items");
        RecyclerView itemRecycler = rootview.findViewById(R.id.item_recyclerview);
        TextView emptyListText = rootview.findViewById(R.id.empty_list_textview);
        TextView totalValueText = rootview.findViewById(R.id.while_value_textview);
        TextView totalInCartValueText = rootview.findViewById(R.id.yellow_value_textview);
        itemRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Item> itemList = new ArrayList<>();
        ItemAdapter itemAdapter = new ItemAdapter(getContext(), itemList);
        itemRecycler.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
        MainActivity.itemViewModel.getListItems(listId).observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                itemAdapter.setItems(items);
                if(items.size() < 1){
                    emptyListText.setVisibility(View.VISIBLE);
                }else{
                    emptyListText.setVisibility(View.GONE);
                }
            }
        });
        MainActivity.itemViewModel.getItemCount(listId).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer itemCount) {
                MainActivity.headerText.setText(listName+" "+"("+itemCount+")");
            }
        });
        MainActivity.itemViewModel.getTotal(listId, false).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                if (total != null) totalValueText.setText(String.valueOf(total));
                else totalValueText.setText(String.valueOf(0.0));
            }
        });
        MainActivity.itemViewModel.getTotal(listId, true).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double total) {
                if (total != null) totalInCartValueText.setText(String.valueOf(total));
                else totalInCartValueText.setText(String.valueOf(0.0));
            }
        });

        addItemBtn.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("listId", listId);
            args.putString("listName", listName);
            AddItemFragment addItemFragment = new AddItemFragment();
            addItemFragment.setArguments(args);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, addItemFragment).commit();
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

    public static void goToEdit(Bundle args, AddItemFragment addItemFragment){
    }
}
