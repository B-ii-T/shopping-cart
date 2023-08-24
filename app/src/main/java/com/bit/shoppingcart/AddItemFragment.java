package com.bit.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class AddItemFragment extends Fragment {
    private EditText itemNameInput, itemUnitPriceInput, itemQntInput;
    Button addItemBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_item_fragment, container, false);
        itemNameInput = rootView.findViewById(R.id.item_name_input);
        itemUnitPriceInput = rootView.findViewById(R.id.unit_price_input);
        itemQntInput = rootView.findViewById(R.id.qnt_input);
        addItemBtn = rootView.findViewById(R.id.add_item_btn);
        int listId = getArguments().getInt("listId", -1);


        addItemBtn.setOnClickListener(V -> {
            if (itemNameInput.getText().toString().trim().isEmpty() ||
                    itemUnitPriceInput.getText().toString().trim().isEmpty() ||
                    itemQntInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "all fields are required", Toast.LENGTH_SHORT).show();
            } else {
                String itemName = itemNameInput.getText().toString().trim();
                Double itemUnitPrice = Double.parseDouble(itemUnitPriceInput.getText().toString().trim());
                int itemQnt = Integer.parseInt(itemQntInput.getText().toString().trim());
                if(itemQnt <= 0 || itemUnitPrice <= 0){
                    Toast.makeText(getContext(), "0 is not valid", Toast.LENGTH_SHORT).show();
                }else{
                    addItem(itemName, itemQnt, itemUnitPrice, listId);
                    Toast.makeText(getContext(), "item added", Toast.LENGTH_SHORT).show();
                    navigateBack(listId);
                }
            }
        });
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBack(listId);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        return rootView;
    }

    public void navigateBack(int listId){
        Bundle args = new Bundle();
        args.putInt("listId", listId);
        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setArguments(args);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, itemFragment)
                .addToBackStack(null)
                .commit();
    }

    private void addItem(String itemName, int qnt, double unitPrice, int listId) {
        MainActivity.itemViewModel.insertItem(new Item(itemName, qnt, unitPrice, listId));
    }
}
