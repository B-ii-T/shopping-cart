package com.bit.shoppingcart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private ItemRepo itemRepo;
    private LiveData<java.util.List<Item>> items;
    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemRepo = new ItemRepo(application);
        items = itemRepo.getItems();
    }
    public void insertItem(Item itm){
        itemRepo.insert(itm);
    }
    public void updateItem(Item itm){
        itemRepo.update(itm);
    }
    public void deleteItem(Item itm){
        itemRepo.delete(itm);
    }
    public void deleteAllItems(){
        itemRepo.deleteAllItems();
    }
    public LiveData<List<Item>> getAllItems() {
        return items;
    }
}