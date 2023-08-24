package com.bit.shoppingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<com.bit.shoppingcart.List> lists;
    private Context context;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int listId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public ListAdapter(Context context, List<com.bit.shoppingcart.List> lists) {
        this.lists = lists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        com.bit.shoppingcart.List currentList = lists.get(position);
        holder.listName.setText(currentList.getListName());
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentList.getId());
            }
        });
        MainActivity.itemViewModel.getItemCount(currentList.getId()).observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer itemCount) {
                holder.listItemCount.setText(itemCount + " items");
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setLists(List<com.bit.shoppingcart.List> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView listName, listItemCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name_textview);
            listItemCount = itemView.findViewById(R.id.list_item_count);
        }
    }
}
