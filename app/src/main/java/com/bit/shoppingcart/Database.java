package com.bit.shoppingcart;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Item.class, List.class}, version = 1)
public abstract class Database extends RoomDatabase {
    private static Database dbInstance;
    public abstract ItemDao itemDao();
    public abstract ListDao listDao();
    public static synchronized Database getDbInstance(Context context){
        if(dbInstance == null){
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "shopping_cart_database")
                    .fallbackToDestructiveMigration().build();
        }
        return dbInstance;
    }
}
