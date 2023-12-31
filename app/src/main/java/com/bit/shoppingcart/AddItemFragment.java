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
        String listName = getArguments().getString("listName", "All items");
        int itemIdArg = getArguments().getInt("itemId", -1);
        int itemQntArg = getArguments().getInt("itemQnt", -1);
        String itemNameArg = getArguments().getString("itemName", "");
        double itemUnitPriceArg = getArguments().getDouble("itemUnitPrice", -1);
        boolean inCart = getArguments().getBoolean("inCart", false);
        if (itemIdArg != -1) {
            itemNameInput.setText(itemNameArg);
            itemQntInput.setText(String.valueOf(itemQntArg));
            itemUnitPriceInput.setText(String.valueOf(itemUnitPriceArg));
            addItemBtn.setText("Save");
        }
        MainActivity.headerText.setText("New item");
        addItemBtn.setOnClickListener(V -> {
            if (itemNameInput.getText().toString().trim().isEmpty() ||
                    itemUnitPriceInput.getText().toString().trim().isEmpty() ||
                    itemQntInput.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "all fields are required", Toast.LENGTH_SHORT).show();
            } else {
                String itemName = itemNameInput.getText().toString().trim();
                Double itemUnitPrice = Double.parseDouble(itemUnitPriceInput.getText().toString().trim());
                int itemQnt = Integer.parseInt(itemQntInput.getText().toString().trim());
                if (itemQnt <= 0 || itemUnitPrice <= 0) {
                    Toast.makeText(getContext(), "0 is not valid", Toast.LENGTH_SHORT).show();
                } else {
                    if (itemIdArg != -1) {
                        Item updatedItem = new Item(itemName, itemQnt, itemUnitPrice, listId, inCart);
                        updatedItem.setId(itemIdArg);
                        MainActivity.itemViewModel.updateItem(updatedItem);
                        navigateBack(listId, listName);
                        Toast.makeText(getContext(), "item updated", Toast.LENGTH_SHORT).show();
                    } else {
                        addItem(itemName, itemQnt, itemUnitPrice, listId, false);
                        Toast.makeText(getContext(), "item added", Toast.LENGTH_SHORT).show();
                        navigateBack(listId, listName);
                    }
                }
            }
        });
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBack(listId, listName);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);

        return rootView;
    }

    public void navigateBack(int listId, String listName) {
        Bundle args = new Bundle();
        args.putInt("listId", listId);
        args.putString("listName", listName);
        ItemFragment itemFragment = new ItemFragment();
        itemFragment.setArguments(args);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, itemFragment)
                .addToBackStack(null)
                .commit();
    }

    private void addItem(String itemName, int qnt, double unitPrice, int listId, boolean inCart) {
        MainActivity.itemViewModel.insertItem(new Item(itemName, qnt, unitPrice, listId, inCart));
    }
}
