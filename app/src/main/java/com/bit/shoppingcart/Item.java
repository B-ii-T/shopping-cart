package com.bit.shoppingcart;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table", foreignKeys = @ForeignKey(entity = List.class,
        parentColumns = "id",
        childColumns = "listId",
        onDelete = ForeignKey.CASCADE))
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String itemName;
    private int itemQuantity;
    private double unitPrice;
    private int listId;

    public Item(String itemName, int itemQuantity, double unitPrice, int listId) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        this.listId = listId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getListId() {
        return listId;
    }
}
