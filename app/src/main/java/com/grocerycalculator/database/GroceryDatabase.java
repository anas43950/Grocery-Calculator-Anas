package com.grocerycalculator.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.grocerycalculator.models.Item;
import com.grocerycalculator.models.Store;


@Database(entities = {Store.class, Item.class}, version = 1, exportSchema = false)
public abstract class GroceryDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "GroceryDB";
    private static GroceryDatabase sInstance;
    private static final Object LOCK = new Object();

    public abstract StoreDao storeDao();

    public abstract ItemDao itemDao();

    public static GroceryDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), GroceryDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            }
        }
        return sInstance;
    }
}
