package com.bit.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ListViewModel listViewModel;
    public static ItemViewModel itemViewModel;
    public static TextView headerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        headerText = findViewById(R.id.header_textview);
        listViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, listFragment).commit();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String qrCodeData = result.getContents();
            JsonObject jsonList = new JsonParser().parse(qrCodeData).getAsJsonObject();
            String listName = jsonList.get("listName").getAsString();
            // Now you can process the qrCodeData to extract the shared list information
            processSharedListData(listName);
            processSharedItems(qrCodeData, listName);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processSharedListData(String listName) {
        try {
            listViewModel.insertList(new List(listName));
        } catch (JsonParseException e) {
            Toast.makeText(this, "Error reading QR code", Toast.LENGTH_SHORT).show();
        }
    }
    private void processSharedItems(String qrCodeData, String listName) {
        try {
            JsonObject jsonList = new JsonParser().parse(qrCodeData).getAsJsonObject();
            JsonArray itemsArray = jsonList.getAsJsonArray("items");
            for (JsonElement itemElement : itemsArray) {
                JsonObject jsonItem = itemElement.getAsJsonObject();
                String itemName = jsonItem.get("itemName").getAsString();
                int quantity = jsonItem.get("quantity").getAsInt();
                double unitPrice = jsonItem.get("unitPrice").getAsDouble();
                boolean inCart = jsonItem.get("inCart").getAsBoolean();
                listViewModel.getListId(listName).observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer id) {
                            Item newItem = new Item(itemName, quantity, unitPrice, id, inCart);
                            itemViewModel.insertItem(newItem);
                    }
                });
            }
        } catch (JsonParseException e) {
            Toast.makeText(this, "Error reading QR code", Toast.LENGTH_SHORT).show();
        }
    }
}