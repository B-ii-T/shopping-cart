package com.bit.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ListViewModel listViewModel;
    public static ItemViewModel itemViewModel;
    public static TextView headerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        headerText = findViewById(R.id.header_textview);
        listViewModel = new ViewModelProvider(this).get(ListViewModel.class);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, listFragment).commit();
    }
}