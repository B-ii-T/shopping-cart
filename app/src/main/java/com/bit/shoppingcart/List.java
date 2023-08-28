package com.bit.shoppingcart;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;


@Entity(tableName = "list_table", indices = {@Index(value = "listName", unique = true)})
public class List {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String listName;

    public List(String listName) {
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
