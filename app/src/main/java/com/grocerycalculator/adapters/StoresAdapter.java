package com.grocerycalculator.adapters;

import static com.grocerycalculator.utils.Constants.AD_VIEW;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.gms.ads.AdView;
import com.grocerycalculator.R;
import com.grocerycalculator.activities.StoreActivity;
import com.grocerycalculator.adapters.ItemsAdapter.AdViewHolder;
import com.grocerycalculator.databinding.ItemStoresRvBinding;
import com.grocerycalculator.databinding.NativeAdLayoutBinding;
import com.grocerycalculator.models.Item;
import com.grocerycalculator.models.Store;
import com.grocerycalculator.utils.AdUtils;
import com.grocerycalculator.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StoresAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    List<Object> objects;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == AD_VIEW) {
            NativeAdLayoutBinding binding = NativeAdLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
            return new AdViewHolder(binding);
        } else {
            ItemStoresRvBinding binding = ItemStoresRvBinding.inflate(LayoutInflater.from(context), parent, false);
            return new StoresViewHolder(binding);
        }
    }

    public StoresAdapter(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object obj = objects.get(position);
        TypedArray colorsArray = context.getResources().obtainTypedArray(R.array.list_view_bg_colors);

        holder.itemView.setBackgroundColor(colorsArray.getColor(position % 5, context.getColor(R.color.light_blue)));
        if (holder.getItemViewType() == AD_VIEW) {
            AdUtils.loadNativeAd(context, ((AdViewHolder) holder).binding);
        } else {
            StoresViewHolder storesViewHolder = (StoresViewHolder) holder;
            Store store = (Store) obj;
            storesViewHolder.binding.storeNameTv.setText(store.getName());
            storesViewHolder.binding.dateTimeTv.setText(getFormattedDate(store.getLastShoppingTimestamp()));

            float discount = store.getTotalSpent() * (store.getDiscount() / 100);
            float tax = (store.getTotalSpent() - discount) * (store.getTax() / 100);
            float finalSpent = store.getTotalSpent() - discount + tax;
            storesViewHolder.binding.totalAmountTv.setText(String.format(Locale.getDefault(), "%s %.2f", store.getCurrencySymbol(), finalSpent));
            storesViewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.currentStore = store;
                    context.startActivity(new Intent(context, StoreActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    @Override
    public int getItemViewType(int position) {
        Object obj = objects.get(position);
        if (obj instanceof Store) {
            return position;
        } else {
            return AD_VIEW;
        }
    }


    static class StoresViewHolder extends RecyclerView.ViewHolder {
        ItemStoresRvBinding binding;

        public StoresViewHolder(@NonNull ItemStoresRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static String getFormattedDate(long timestamp) {
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(timestamp);
    }
}