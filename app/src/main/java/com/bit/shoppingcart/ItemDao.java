package com.bit.shoppingcart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert (Item item);
    @Update
    void update (Item item);
    @Delete
    void delete (Item item);
    @Query("DELETE FROM item_table")
    void deleteAll();
    @Query("SELECT * FROM item_table")
    LiveData<List<Item>> getAllItems();
    @Query("SELECT count(*) FROM item_table WHERE listId = :listId")
    LiveData<Integer> listItemCount(int listId);
    @Query("SELECT * FROM item_table WHERE listId = :listId")
    LiveData<List<Item>> getListItems(int listId);
    @Query("SELECT (itemQuantity * unitPrice) AS total FROM item_table WHERE id = :itemId")
    LiveData<Double> getTotalById(int itemId);
    @Query("SELECT sum(itemQuantity * unitPrice) AS total FROM item_table WHERE listId = :listId")
    LiveData<Double> getTotal(int listId);
}
