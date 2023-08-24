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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private long lastBackPressedTime = 0;
    private static final long DOUBLE_TAP_EXIT_INTERVAL = 2000; // 2 seconds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.list_list, container, false);
        RecyclerView listRecycler = rootview.findViewById(R.id.list_recyclerview);
        listRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ArrayList<List> listList = new ArrayList<>();
        ListAdapter listAdapter = new ListAdapter(getContext(), listList);
        listRecycler.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        listAdapter.setOnItemClickListener(listId -> {
            Bundle args = new Bundle();
            args.putInt("listId", listId);
            ItemFragment itemFragment = new ItemFragment();
            itemFragment.setArguments(args);
            requireFragmentManager().beginTransaction().replace(R.id.frame, itemFragment).commit();
        });
        MainActivity.listViewModel.getAllLists().observe(getViewLifecycleOwner(), new Observer<java.util.List<List>>() {
            @Override
            public void onChanged(java.util.List<List> lists) {
                listAdapter.setLists(lists);
            }
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
        requireActivity().getOnBackPressedDispatcher().addCallback(this, doubleTapExitCallback);

        return rootview;
    }
}
