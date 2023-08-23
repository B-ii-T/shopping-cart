package com.bit.shoppingcart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private ListRepo listRepo;
    private LiveData<List<com.bit.shoppingcart.List>> lists;
    public ListViewModel(@NonNull Application application) {
        super(application);
        listRepo = new ListRepo(application);
        lists = listRepo.getLists();
    }
    public void insertList(com.bit.shoppingcart.List lst){
        listRepo.insert(lst);
    }
    public void updateList(com.bit.shoppingcart.List lst){
        listRepo.update(lst);
    }
    public void deleteList(com.bit.shoppingcart.List lst){
        listRepo.delete(lst);
    }
    public void deleteAllLists(){
        listRepo.deleteAllLists();
    }
    public LiveData<List<com.bit.shoppingcart.List>> getAllLists() {
        return lists;
    }
}
