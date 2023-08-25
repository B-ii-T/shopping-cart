package com.bit.shoppingcart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;

    public ItemAdapter(Context context, List<Item> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item currentItem = items.get(position);
        holder.itemName.setText(currentItem.getItemName());
        holder.itemQnt.setText(String.valueOf(currentItem.getItemQuantity()));
        MainActivity.itemViewModel.getItemTotal(currentItem.getId()).observe((LifecycleOwner) context, new Observer<Double>() {
            @Override
            public void onChanged(Double itemTotal) {
                holder.totalPrice.setText(String.valueOf(itemTotal));
            }
        });
        holder.plusBtn.setOnClickListener(v -> {
            currentItem.setItemQuantity(currentItem.getItemQuantity() + 1);
            MainActivity.itemViewModel.updateItem(currentItem);
        });
        holder.minusBtn.setOnClickListener(v -> {
            if (currentItem.getItemQuantity() > 1) {
                currentItem.setItemQuantity(currentItem.getItemQuantity() - 1);
            }
            MainActivity.itemViewModel.updateItem(currentItem);
        });
        holder.itemDotsMenu.setOnClickListener(v -> {
            showItemMenu(v, currentItem);
        });
        holder.cartBtn.setOnClickListener(v -> {
            if(currentItem.isInCart()) {
                currentItem.setInCart(false);
                MainActivity.itemViewModel.updateItem(currentItem);
                holder.cartBtn.setImageResource(R.drawable.white_cart);
            }else{
                currentItem.setInCart(true);
                MainActivity.itemViewModel.updateItem(currentItem);
                holder.cartBtn.setImageResource(R.drawable.yellow_cart);
            }
        });
    }

    private void showItemMenu(View v, Item currentItem) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v); // Pass the context and the anchor view
        popupMenu.inflate(R.menu.item_menu); // Inflate the menu resource

        // Set up a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.delete_item) {
                    MainActivity.itemViewModel.deleteItem(currentItem);
                    notifyDataSetChanged();
                    return true;
                } else if (item.getItemId() == R.id.update_item) {
                    Bundle args = new Bundle();
                    args.putInt("itemId", currentItem.getId());
                    args.putString("itemName", currentItem.getItemName());
                    args.putDouble("itemUnitPrice", currentItem.getUnitPrice());
                    args.putInt("itemQnt", currentItem.getItemQuantity());
                    args.putInt("listId", currentItem.getListId());
                    args.putString("listName", ItemFragment.listName);
                    args.putBoolean("inCart", currentItem.isInCart());
                    AddItemFragment addItemFragment = new AddItemFragment();
                    addItemFragment.setArguments(args);
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame, addItemFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show(); // Show the popup menu
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQnt, totalPrice;
        CardView plusBtn, minusBtn;
        ImageButton itemDotsMenu;
        ImageView cartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemQnt = itemView.findViewById(R.id.item_qnt_value);
            totalPrice = itemView.findViewById(R.id.total_value);
            plusBtn = itemView.findViewById(R.id.plus_btn);
            minusBtn = itemView.findViewById(R.id.minus_btn);
            itemDotsMenu = itemView.findViewById(R.id.item_dots_menu);
            cartBtn = itemView.findViewById(R.id.item_cart_btn);
        }
    }
}
