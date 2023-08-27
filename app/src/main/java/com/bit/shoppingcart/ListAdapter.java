package com.bit.shoppingcart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.WriterException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<com.bit.shoppingcart.List> lists;
    private Context context;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int listId, String listName);
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
                itemClickListener.onItemClick(currentList.getId(), currentList.getListName());
            }
        });
        holder.qrCodeBtn.setOnClickListener(v -> {
            MainActivity.itemViewModel.getListItems(currentList.getId()).observe((LifecycleOwner) context, new Observer<List<Item>>() {
                @Override
                public void onChanged(List<Item> items) {
                    String sharedList = generateSharedListJson(currentList.getListName(), items);
                    Bitmap qrCodeBitmap = generateQRCode(sharedList);
                    if (qrCodeBitmap != null) {
                        showQRCodeDialog(qrCodeBitmap);
                    } else {
                        // Handle QR code generation failure
                        Toast.makeText(context, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        holder.deleteListBtn.setOnClickListener(v -> {
            showDeleteConfirmationDialog(v, currentList);
        });
        holder.itemView.setOnLongClickListener(v -> {
            showUpdateListDialog(v, currentList);
            return true;
        });
        MainActivity.itemViewModel.getItemCount(currentList.getId()).observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer itemCount) {
                holder.listItemCount.setText(itemCount + " items");
            }
        });
    }

    private String generateSharedListJson(String listName, List<Item> items) {
        JsonObject jsonList = new JsonObject();
        jsonList.addProperty("listName", listName);

        JsonArray itemsArray = new JsonArray();
        for (Item item : items) {
            JsonObject jsonItem = new JsonObject();
            jsonItem.addProperty("itemName", item.getItemName());
            jsonItem.addProperty("quantity", item.getItemQuantity());
            jsonItem.addProperty("unitPrice", item.getUnitPrice());
            jsonItem.addProperty("inCart", item.isInCart());
            itemsArray.add(jsonItem);
        }
        jsonList.add("items", itemsArray);

        return jsonList.toString();
    }

    private Bitmap generateQRCode(String data) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void showQRCodeDialog(Bitmap qrCodeBitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.qr_code_dialog, null);
        ImageView qrCodeImageView = dialogView.findViewById(R.id.qr_code_image_view);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);

        builder.setView(dialogView);
        builder.setPositiveButton("Close", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showDeleteConfirmationDialog(View view, com.bit.shoppingcart.List currentList) {
        Context context = view.getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete")
                .setMessage("All tasks within ths list will be deleted!\nAre you sure you want to delete this list?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.listViewModel.deleteList(currentList);
                        Toast.makeText(context, "list deleted", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showUpdateListDialog(View v, com.bit.shoppingcart.List list) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext());
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        View dialogView = inflater.inflate(R.layout.add_list_dialog, null);
        builder.setView(dialogView);

        EditText listNameInput = dialogView.findViewById(R.id.list_name_input);
        Button createListBtn = dialogView.findViewById(R.id.create_list_btn);

        listNameInput.setText(list.getListName());
        createListBtn.setText("Save");

        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(true);

        createListBtn.setOnClickListener(V -> {
            if(listNameInput.getText().toString().trim().isEmpty()){
                listNameInput.setError("Required");
            }else{
                com.bit.shoppingcart.List updatedList = new com.bit.shoppingcart.List(listNameInput.getText().toString().trim());
                updatedList.setId(list.getId());
                MainActivity.listViewModel.updateList(updatedList);
                Toast.makeText(v.getContext(), "List updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
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
        ImageView deleteListBtn, qrCodeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.list_name_textview);
            listItemCount = itemView.findViewById(R.id.list_item_count);
            deleteListBtn = itemView.findViewById(R.id.delete_list_btn);
            qrCodeBtn = itemView.findViewById(R.id.qr_code_btn);
        }
    }
}
