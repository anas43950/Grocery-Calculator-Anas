package com.grocerycalculator.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.grocerycalculator.adapters.CurrencyAdapter.OnCurrencySelectedListener;
import com.grocerycalculator.adapters.StoresAdapter;
import com.grocerycalculator.database.GroceryDatabase;
import com.grocerycalculator.databinding.ActivityMainBinding;
import com.grocerycalculator.dialogs.CurrencyPickerDialog;
import com.grocerycalculator.dialogs.MyCustomDialog;
import com.grocerycalculator.dialogs.MyCustomDialog.OnNegativeClickedListener;
import com.grocerycalculator.dialogs.MyCustomDialog.OnPositiveClickedListener;
import com.grocerycalculator.enums.DialogTask;
import com.grocerycalculator.models.Currency;
import com.grocerycalculator.models.Store;
import com.grocerycalculator.utils.Constants;
import com.grocerycalculator.utils.Constants.SharedPrefsConstants;
import com.grocerycalculator.utils.CurrencyData;
import com.grocerycalculator.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity implements OnCurrencySelectedListener, OnPositiveClickedListener, OnNegativeClickedListener {
    ActivityMainBinding binding;
    CurrencyPickerDialog currencyPickerDialog;
    ImageView currencyPickerIv;
    public static final int CURRENCY_PICKER_ID = 1231;
    StoresAdapter adapter;
    List<Store> stores;
    GroceryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot(), this, "");
        FirebaseAnalytics.getInstance(this);
        MobileAds.initialize(this);

        // Adding it dynamically because we don't want it to appear in rest of the activities
        addCurrencyPickerInToolbar();
        setListeners();
        Constants.USE_SYSTEM_KEYBOARD = SharedPrefsUtils.getBooleanData(this, SharedPrefsConstants.USE_SYSTEM_KEYBOARD);
    }


    @Override
    protected void onResume() {
        super.onResume();
        db = GroceryDatabase.getInstance(this);
        stores = db.storeDao().loadAllStores();
        binding.totalStoresTv.setText(String.format(Locale.getDefault(), "Total Stores : %d", stores.size()));

        Collections.reverse(stores);
        List<Object> objects = new ArrayList<>(stores);
        final int FIRST_ADD_POSITION = 4;
        final int AD_STEP_SIZE = 5;
        for (int i = FIRST_ADD_POSITION; i < objects.size(); i = i + AD_STEP_SIZE) {
            objects.add((i), "AD_VIEW");
        }

        adapter = new StoresAdapter(objects);
        binding.mainRv.setLayoutManager(new LinearLayoutManager(this));
        binding.mainRv.setAdapter(adapter);
    }

    private void showCurrencyPickerDialog() {
        currencyPickerDialog = new CurrencyPickerDialog(this, this);
        currencyPickerDialog.show();
    }

    @Override
    public void onCurrencySelected(Currency currency) {

        Constants.selectedCurrency = currency;
        SharedPrefsUtils.saveData(this, SharedPrefsConstants.SELECTED_CURRENCY_CODE, currency.getCountryCode());
        Glide.with(this).load("file:///android_asset/flags/" + currency.getFlagName() + ".png").into(currencyPickerIv);
        if (currencyPickerDialog.isShowing()) {
            currencyPickerDialog.dismiss();
        }
    }

    private void setListeners() {
        findViewById(CURRENCY_PICKER_ID).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrencyPickerDialog();
            }
        });
        binding.addStoreTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomDialog addStoreDialog = new MyCustomDialog(MainActivity.this, MainActivity.this, DialogTask.ADD_STORE);
                addStoreDialog.setOnNegativeClickedListener(MainActivity.this);
                addStoreDialog.show();
            }
        });
    }

    private void addCurrencyPickerInToolbar() {
        String selectedCountryCode = SharedPrefsUtils.getStringData(this, SharedPrefsConstants.SELECTED_CURRENCY_CODE, SharedPrefsConstants.DEFAULT_CURRENCY_CODE);
        Constants.selectedCurrency = CurrencyData.getCurrency(selectedCountryCode);
        currencyPickerIv = new ImageView(this);
        currencyPickerIv.setId(CURRENCY_PICKER_ID);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(75, 75);
        binding.appbar.toolbar.addView(currencyPickerIv);
        params.setMarginEnd(40);
        params.gravity = Gravity.END;
        currencyPickerIv.setLayoutParams(params);
        Glide.with(this).load("file:///android_asset/flags/" + Constants.selectedCurrency.getFlagName() + ".png").into(currencyPickerIv);
    }


    @Override
    public void onPositiveClicked(String input, DialogTask dialogTask) {
        addStore(input);
    }

    @Override
    public void onNegativeClicked() {
        addStore(null);
    }

    private void addStore(String name) {
        if (name == null) {
            name = "Store " + (stores.size() + 1);
        }
        Store store = new Store(name, 0, 0, 0, 0, System.currentTimeMillis(), Constants.selectedCurrency.getCurrencySymbol());
        long storeId = db.storeDao().insertStore(store);
        store.setId((int) storeId);
        Constants.currentStore = store;
        startActivity(new Intent(this, StoreActivity.class));
    }
}