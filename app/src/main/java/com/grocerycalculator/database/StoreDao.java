package com.grocerycalculator.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.grocerycalculator.models.Store;

import java.util.List;

@Dao
public interface StoreDao {

    @Query("SELECT * FROM stores")
    List<Store> loadAllStores();

    @Insert(onConflict = REPLACE)
    long insertStore(Store store);

    @Query("SELECT * FROM stores WHERE id = :id")
    LiveData<Store> getStore(int id);

    @Delete
    void deleteStore(Store store);

    @Query("DELETE FROM stores")
    void deleteAllRecipes();

    @Query("UPDATE stores SET name = :name WHERE id = :id")
    void updateName(int id, String name);

    @Query("UPDATE stores SET tax = :tax WHERE id = :id")
    void updateTax(int id, float tax);

    @Query("UPDATE stores SET discount = :discount WHERE id = :id")
    void updateDiscount(int id, float discount);

    @Query("UPDATE stores SET budget = :budget WHERE id = :id")
    void updateBudget(int id, float budget);

    @Query("UPDATE stores SET totalSpent = :totalSpent WHERE id = :id")
    void updateTotalSpent(int id, float totalSpent);

    @Query("UPDATE stores SET lastShoppingTimestamp = :timestamp WHERE id = :id")
    void updateLastShoppingTimestamp(int id, long timestamp);
}
