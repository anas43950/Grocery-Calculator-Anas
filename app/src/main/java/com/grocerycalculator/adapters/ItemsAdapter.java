package com.grocerycalculator.adapters;

import static com.grocerycalculator.utils.Constants.AD_VIEW;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.grocerycalculator.R;
import com.grocerycalculator.databinding.ItemsRvItemBinding;
import com.grocerycalculator.databinding.NativeAdLayoutBinding;
import com.grocerycalculator.dialogs.MyCustomDialog;
import com.grocerycalculator.dialogs.MyCustomDialog.OnPositiveClickedListener;
import com.grocerycalculator.enums.DialogTask;
import com.grocerycalculator.models.Item;
import com.grocerycalculator.utils.AdUtils;
import com.grocerycalculator.utils.Constants;

import java.lang.annotation.Native;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Object> objects;
    OnItemAlteredListener onItemAlteredListener;
    OnPositiveClickedListener onPositiveClickedListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == AD_VIEW) {
            NativeAdLayoutBinding binding = NativeAdLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
            return new AdViewHolder(binding);
        } else {
            ItemsRvItemBinding binding = ItemsRvItemBinding.inflate(LayoutInflater.from(context), parent, false);
            return new ItemViewHolder(binding);
        }
    }

    public ItemsAdapter(List<Object> objects, OnItemAlteredListener onItemAlteredListener, OnPositiveClickedListener onPositiveClickedListener) {
        this.objects = objects;
        this.onItemAlteredListener = onItemAlteredListener;
        this.onPositiveClickedListener = onPositiveClickedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TypedArray colorsArray = context.getResources().obtainTypedArray(R.array.list_view_bg_colors);
        holder.itemView.setBackgroundColor(colorsArray.getColor(position % 5, context.getColor(R.color.light_blue)));
        Object obj = objects.get(position);
        if (holder.getItemViewType() == AD_VIEW) {
            AdUtils.loadNativeAd(context, ((AdViewHolder) holder).binding);
        } else {
            Item item = (Item) obj;
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.binding.itemNameTv.setText(item.getName());
            itemViewHolder.binding.menuBtn.setOnClickListener(view -> {
                showPopupMenu(itemViewHolder.binding, item);
            });
            itemViewHolder.binding.quantityTv.setText(item.getQuantity() + "");
            itemViewHolder.binding.priceTv.setText(Constants.currentStore.getCurrencySymbol() + " " + item.getPrice());
            itemViewHolder.binding.increaseQuantityBtn.setOnClickListener(view -> {
                item.setQuantity(item.getQuantity() + 1);
                onItemAlteredListener.onQuantityChanged(item, true);
            });
            itemViewHolder.binding.decreaseQuantityBtn.setOnClickListener(view -> {
                item.setQuantity(item.getQuantity() - 1);
                onItemAlteredListener.onQuantityChanged(item, false);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = objects.get(position);
        if (obj instanceof Item) {
            return position;
        } else {
            return AD_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemsRvItemBinding binding;

        public ItemViewHolder(@NonNull ItemsRvItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void showPopupMenu(ItemsRvItemBinding binding, Item item) {
        Constants.currentItem = item;
        PopupMenu popup = new PopupMenu(context, binding.menuBtn);
        popup.getMenuInflater()
                .inflate(R.menu.item_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.delete_item_menu) {
                onItemAlteredListener.onDeleteItemClicked(item);
                return true;
            }
            if (menuItem.getItemId() == R.id.item_name_menu) {
                new MyCustomDialog(context, onPositiveClickedListener, DialogTask.ITEM_NAME).show();
                return true;
            }
            if (menuItem.getItemId() == R.id.add_quantity_menu) {
                new MyCustomDialog(context, onPositiveClickedListener, DialogTask.ITEM_QUANTITY).show();
                return true;
            }
            if (menuItem.getItemId() == R.id.edit_price_menu) {
                new MyCustomDialog(context, onPositiveClickedListener, DialogTask.ITEM_PRICE).show();
                return true;
            }
            return false;
        });
        popup.show();
    }

    public interface OnItemAlteredListener {
        void onDeleteItemClicked(Item item);

        void onQuantityChanged(Item item, boolean isIncreased);
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        NativeAdLayoutBinding binding;

        public AdViewHolder(@NonNull NativeAdLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}