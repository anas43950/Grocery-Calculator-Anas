package com.grocerycalculator.utils;

import com.grocerycalculator.models.Currency;
import com.grocerycalculator.models.Item;
import com.grocerycalculator.models.Store;

public class Constants {
    public static Currency selectedCurrency;
    public static Store currentStore;
    public static Item currentItem;
    public static boolean USE_SYSTEM_KEYBOARD = false;
    public static final int AD_VIEW = -11;
    public static class SharedPrefsConstants {
        public static final String SHARED_PREF_NAME = "general";
        public static final String SELECTED_CURRENCY_CODE = "selected_currency_code";
        public static final String DEFAULT_CURRENCY_CODE = "USD";
        public static final String USE_SYSTEM_KEYBOARD = "use_system_keyboard";
    }
}
