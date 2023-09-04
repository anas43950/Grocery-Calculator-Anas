package com.grocerycalculator.enums;

public enum DialogTask {
    ADD_STORE("Store Name"),
    EDIT_STORE_NAME("Edit Store Name"),
    TAX("Tax"),
    BUDGET("Budget"),
    DISCOUNT("Discount"),
    ITEM_NAME("Edit Item Name"),
    ITEM_QUANTITY("Edit Quantity"),
    ITEM_PRICE("Edit Item Price");
    String title;

    DialogTask(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
