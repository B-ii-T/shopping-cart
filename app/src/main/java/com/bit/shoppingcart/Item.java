package com.bit.shoppingcart;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table", foreignKeys = @ForeignKey(entity = List.class,
        parentColumns = "id",
        childColumns = "listId",
        onDelete = ForeignKey.CASCADE), indices = {@Index(value = "itemName", unique = true)})
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String itemName;
    private int itemQuantity;
    private double unitPrice;
    private int listId;
    private boolean inCart;

    public Item(String itemName, int itemQuantity, double unitPrice, int listId, boolean inCart) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        this.listId = listId;
        this.inCart = inCart;
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

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getListId() {
        return listId;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }
}
