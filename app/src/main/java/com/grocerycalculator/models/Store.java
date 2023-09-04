package com.grocerycalculator.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "stores")
public class Store {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private float totalSpent;
    private float budget;
    private float tax;
    private float discount;
    private long lastShoppingTimestamp;
    private String currencySymbol;

    public Store(int id, String name, float totalSpent, float budget, float tax, float discount, long lastShoppingTimestamp, String currencySymbol) {
        this.id = id;
        this.name = name;
        this.totalSpent = totalSpent;
        this.budget = budget;
        this.tax = tax;
        this.discount = discount;
        this.lastShoppingTimestamp = lastShoppingTimestamp;
        this.currencySymbol = currencySymbol;
    }


    @Ignore
    public Store(String name, float totalSpent, float budget, float tax, float discount, long lastShoppingTimestamp, String currencySymbol) {
        this.name = name;
        this.totalSpent = totalSpent;
        this.budget = budget;
        this.tax = tax;
        this.discount = discount;
        this.lastShoppingTimestamp = lastShoppingTimestamp;
        this.currencySymbol = currencySymbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(float totalSpent) {
        this.totalSpent = totalSpent;
    }

    public float getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public long getLastShoppingTimestamp() {
        return lastShoppingTimestamp;
    }

    public void setLastShoppingTimestamp(long lastShoppingTimestamp) {
        this.lastShoppingTimestamp = lastShoppingTimestamp;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
