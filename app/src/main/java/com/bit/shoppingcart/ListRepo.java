package com.bit.shoppingcart;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ListRepo {
    private ListDao listDao;
    private LiveData<List<com.bit.shoppingcart.List>> lists;

    public ListRepo(Application app) {
        Database database = Database.getDbInstance(app);
        listDao = database.listDao();
        lists = listDao.getAllLists();
    }
    public void insert(com.bit.shoppingcart.List list){
        new InsertAsync(listDao).execute(list);
    }
    public void update(com.bit.shoppingcart.List list){
        new UpdateAsync(listDao).execute(list);
    }
    public void delete(com.bit.shoppingcart.List list){
        new DeleteAsync(listDao).execute(list);
    }
    public void deleteAllLists(){
        new DeleteAllAsync(listDao).execute();
    }

    public LiveData<List<com.bit.shoppingcart.List>> getLists() {
        return lists;
    }

    private static class InsertAsync extends AsyncTask<com.bit.shoppingcart.List, Void, Void>{
        private ListDao listDao;
        private InsertAsync(ListDao listDao){this.listDao = listDao;}
        @Override
        protected Void doInBackground(com.bit.shoppingcart.List... lists) {
            listDao.insert(lists[0]);
            return null;
        }
    }
    private static class UpdateAsync extends AsyncTask<com.bit.shoppingcart.List, Void, Void>{
        private ListDao listDao;
        private UpdateAsync(ListDao listDao){this.listDao = listDao;}
        @Override
        protected Void doInBackground(com.bit.shoppingcart.List... lists) {
            listDao.update(lists[0]);
            return null;
        }
    }
    private static class DeleteAsync extends AsyncTask<com.bit.shoppingcart.List, Void, Void>{
        private ListDao listDao;
        private DeleteAsync(ListDao listDao){this.listDao = listDao;}
        @Override
        protected Void doInBackground(com.bit.shoppingcart.List... lists) {
            listDao.delete(lists[0]);
            return null;
        }
    }
    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private ListDao listDao;
        private DeleteAllAsync(ListDao listDao){this.listDao = listDao;}
        @Override
        protected Void doInBackground(Void... voids) {
            listDao.deleteAll();
            return null;
        }
    }
}
