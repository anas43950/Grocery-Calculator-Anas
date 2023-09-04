package com.grocerycalculator.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.grocerycalculator.models.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE storeId = :storeId")
    LiveData<List<Item>> loadAllItemsForThisStore(int storeId);

    @Insert(onConflict = REPLACE)
    void insertItem(Item item);

    @Query("SELECT * FROM items WHERE id = :id")
    Item getItem(int id);

    @Delete
    int deleteItem(Item item);

    @Query("DELETE FROM items WHERE storeId = :storeId")
    void deleteAllItems(int storeId);

    @Query("UPDATE items SET quantity = :quantity WHERE id = :id")
    void updateQuantity(int id, int quantity);

    @Query("UPDATE items SET name = :name WHERE id = :id")
    void updateName(int id, String name);

    @Query("UPDATE items SET price = :price WHERE id = :id")
    void updatePrice(int id, float price);

}
