package com.bit.shoppingcart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ListViewModel extends AndroidViewModel {
    private ListRepo listRepo;
    private LiveData<List<com.bit.shoppingcart.List>> lists;
    private LiveData<Integer> listCount;
    private LiveData<Integer> listId;
    public ListViewModel(@NonNull Application application) {
        super(application);
        listRepo = new ListRepo(application);
        lists = listRepo.getLists();
        listCount = listRepo.getListCount();
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
    public LiveData<Integer> getListCount() {
        return listCount;
    }
    public LiveData<Integer> getListId(String listName) {
        listId = listRepo.getListId(listName);
        return listId;
    }
}
