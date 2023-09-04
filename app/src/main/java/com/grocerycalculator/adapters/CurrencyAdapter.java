package com.grocerycalculator.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.grocerycalculator.R;
import com.grocerycalculator.databinding.CurrencyPickerItemBinding;
import com.grocerycalculator.models.Currency;
import com.grocerycalculator.utils.AdUtils;
import com.grocerycalculator.utils.AdUtils.OnLoadInterstitialFailedListener;
import com.grocerycalculator.utils.Constants;
import com.grocerycalculator.utils.CurrencyData;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    Context context;
    List<Currency> currencies = CurrencyData.getCurrenciesList();
    OnCurrencySelectedListener listener;
    List<Currency> currenciesCopy = new ArrayList<>();

    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CurrencyPickerItemBinding binding = CurrencyPickerItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    public CurrencyAdapter(OnCurrencySelectedListener listener, Activity activity) {
        this.listener = listener;
        this.context = activity;
        currenciesCopy.addAll(currencies);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        Currency currency = currenciesCopy.get(position);
        holder.binding.currencyItemCountryCodeTv.setText(currency.getCountryCode());
        holder.binding.currencyItemCountryNameTv.setText(currency.getCountryName());
        holder.binding.currencySymbolTv.setText(currency.getCurrencySymbol());
        if (currency == Constants.selectedCurrency) {
            int green = context.getResources().getColor(R.color.light_green, null);
            holder.binding.currencySymbolTv.setTextColor(green);
            holder.binding.currencyItemCountryNameTv.setTextColor(green);
            holder.binding.currencyItemCountryCodeTv.setTextColor(green);
        }
        Glide.with(context).load("file:///android_asset/flags/" + currency.getFlagName() + ".png").into(holder.binding.currencyItemFlagIv);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FullScreenContentCallback contentCallback = new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        listener.onCurrencySelected(currency);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        listener.onCurrencySelected(currency);
                    }
                };
                AdUtils.showInterstitialAd((Activity) context, contentCallback, new OnLoadInterstitialFailedListener() {
                    @Override
                    public void onLoadInterstitialFailed() {
                        listener.onCurrencySelected(currency);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return currenciesCopy.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CurrencyPickerItemBinding binding;

        public ViewHolder(@NonNull CurrencyPickerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnCurrencySelectedListener {
        void onCurrencySelected(Currency currency);
    }

    public void filterCurrencies(String text) {
        text = text.toLowerCase();
        currenciesCopy.removeAll(currenciesCopy);
        for (Currency currency : currencies) {
            if (currency.getCountryName().toLowerCase().contains(text) || currency.getCurrencySymbol().equals(text) || currency.getCountryCode().toLowerCase().contains(text)) {
                currenciesCopy.add(currency);
            }
        }
        notifyDataSetChanged();
    }

    public void showAll() {
        currenciesCopy.removeAll(currenciesCopy);
        currenciesCopy.addAll(currencies);
        notifyDataSetChanged();
    }
}