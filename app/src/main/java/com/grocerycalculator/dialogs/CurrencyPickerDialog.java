package com.grocerycalculator.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.grocerycalculator.adapters.CurrencyAdapter;
import com.grocerycalculator.adapters.CurrencyAdapter.OnCurrencySelectedListener;
import com.grocerycalculator.databinding.CurrencyPickerDialogBinding;
import com.grocerycalculator.databinding.CurrencyPickerDialogBinding;

public class CurrencyPickerDialog extends Dialog {
    CurrencyPickerDialogBinding binding;
    OnCurrencySelectedListener listener;
    Activity activity;
    public CurrencyPickerDialog(@NonNull Activity activity, OnCurrencySelectedListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CurrencyPickerDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(true);

        InsetDrawable inset = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 150, 0, 0, 0);
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(inset);
        getWindow().setGravity(Gravity.TOP | Gravity.END);


        binding.currencyRv.setLayoutManager(new LinearLayoutManager(getContext()));
        CurrencyAdapter adapter = new CurrencyAdapter(listener, activity);
        binding.currencyRv.setAdapter(adapter);
        binding.searchCurrencyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")) {
                    adapter.showAll();
                } else {
                    adapter.filterCurrencies(editable.toString());
                }
            }
        });
    }


}
