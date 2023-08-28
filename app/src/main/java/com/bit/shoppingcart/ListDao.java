package com.bit.shoppingcart;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(com.bit.shoppingcart.List list);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(com.bit.shoppingcart.List list);

    @Delete
    void delete(com.bit.shoppingcart.List list);

    @Query("DELETE FROM list_table")
    void deleteAll();

    @Query("SELECT * FROM list_table")
    LiveData<List<com.bit.shoppingcart.List>> getAllLists();

    @Query("SELECT count(*) FROM list_table")
    LiveData<Integer> getListCount();

    @Query("SELECT id FROM list_table WHERE listName = :listName")
    LiveData<Integer> getListId(String listName);
}
