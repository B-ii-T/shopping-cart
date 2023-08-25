package com.bit.shoppingcart;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ItemRepo {
    private ItemDao itemDao;
    private LiveData<List<Item>> items;
    private LiveData<List<Item>> listItems;
    private LiveData<Integer> listItemCount;
    private LiveData<Double> itemTotal;
    private LiveData<Double> total;

    public ItemRepo(Application app) {
        Database database = Database.getDbInstance(app);
        itemDao = database.itemDao();
        items = itemDao.getAllItems();
    }

    public void insert(Item item) {
        new InsertAsync(itemDao).execute(item);
    }

    public void update(Item item) {
        new UpdateAsync(itemDao).execute(item);
    }

    public void delete(Item item) {
        new DeleteAsync(itemDao).execute(item);
    }

    public void deleteAllItems() {
        new DeleteAllAsync(itemDao).execute();
    }

    public LiveData<List<Item>> getItems() {
        return items;
    }

    public LiveData<Integer> getItemCount(int listId) {
        listItemCount = itemDao.listItemCount(listId);
        return listItemCount;
    }

    public LiveData<List<Item>> getListItems(int listId) {
        listItems = itemDao.getListItems(listId);
        return listItems;
    }

    public LiveData<Double> getItemTotal(int itemId) {
        itemTotal = itemDao.getTotalById(itemId);
        return itemTotal;
    }
    public LiveData<Double> getTotal(int listId) {
        total = itemDao.getTotal(listId);
        return total;
    }

    private static class InsertAsync extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private InsertAsync(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }

    private static class UpdateAsync extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private UpdateAsync(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        private DeleteAsync(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private ItemDao itemDao;

        private DeleteAllAsync(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.deleteAll();
            return null;
        }
    }
}
