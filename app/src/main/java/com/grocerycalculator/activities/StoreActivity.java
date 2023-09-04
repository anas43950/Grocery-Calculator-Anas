package com.grocerycalculator.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.grocerycalculator.R;
import com.grocerycalculator.adapters.ItemsAdapter;
import com.grocerycalculator.adapters.ItemsAdapter.OnItemAlteredListener;
import com.grocerycalculator.database.GroceryDatabase;
import com.grocerycalculator.databinding.ActivityStoreBinding;
import com.grocerycalculator.dialogs.AddItemDialog;
import com.grocerycalculator.dialogs.AddItemDialog.OnAddClickedListener;
import com.grocerycalculator.dialogs.MyCustomDialog;
import com.grocerycalculator.dialogs.MyCustomDialog.OnPositiveClickedListener;
import com.grocerycalculator.enums.DialogTask;
import com.grocerycalculator.models.Item;
import com.grocerycalculator.models.Store;
import com.grocerycalculator.utils.AdUtils;
import com.grocerycalculator.utils.AdUtils.OnLoadInterstitialFailedListener;
import com.grocerycalculator.utils.Constants;
import com.grocerycalculator.utils.Constants.SharedPrefsConstants;
import com.grocerycalculator.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StoreActivity extends BaseActivity implements OnPositiveClickedListener, OnItemAlteredListener, OnAddClickedListener {
    ItemsAdapter adapter;
    ActivityStoreBinding binding;
    LiveData<Store> store;
    LiveData<List<Item>> items;
    GroceryDatabase db;
    float discount, tax, finalSpent, remaining;
    AddItemDialog addItemDialog;
    int currentStoreId = Constants.currentStore.getId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot(), this, Constants.currentStore.getName(), true);

        db = GroceryDatabase.getInstance(this);
        items = db.itemDao().loadAllItemsForThisStore(currentStoreId);
        items.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                Collections.reverse(items);
                List<Object> objects = new ArrayList<>(items);
                final int FIRST_ADD_POSITION = 4;
                final int AD_STEP_SIZE = 5;
                for (int i = FIRST_ADD_POSITION; i < objects.size(); i = i + AD_STEP_SIZE) {
                    objects.add((i), "AD_VIEW");
                }
                adapter = new ItemsAdapter(objects, StoreActivity.this, StoreActivity.this);
                binding.itemsRv.setLayoutManager(new LinearLayoutManager(StoreActivity.this));
                binding.itemsRv.setAdapter(adapter);
            }
        });
        store = db.storeDao().getStore(currentStoreId);
        store.observe(this, new Observer<Store>() {
            @Override
            public void onChanged(Store store) {
                Constants.currentStore = store;
                updateExpensesInfoViews(store);
            }
        });
        setListeners();
    }

    private void updateExpensesInfoViews(Store store) {
        discount = store.getTotalSpent() * (store.getDiscount() / 100);
        tax = (store.getTotalSpent() - discount) * (store.getTax() / 100);
        finalSpent = store.getTotalSpent() - discount + tax;
        remaining = store.getBudget() - finalSpent;
        if (store.getBudget() == 0) {
            changeBudgetInfoVisibility(View.GONE);
        } else {
            changeBudgetInfoVisibility(View.VISIBLE);
        }
        if (discount == 0 && tax == 0) {
            changeDiscountAndTaxInfoVisibility(View.GONE);
        } else {
            if (addItemDialog != null && !addItemDialog.isShowing()) {
                changeDiscountAndTaxInfoVisibility(View.VISIBLE);
            }
        }
        binding.discountTv.setText(String.format(Locale.getDefault(), "Discount (%.2f%%)\n%s%.2f", store.getDiscount(), store.getCurrencySymbol(), discount));
        binding.spentTv.setText(String.format(Locale.getDefault(), "Spent\n%s%.2f", store.getCurrencySymbol(), store.getTotalSpent()));
        binding.taxTv.setText(String.format(Locale.getDefault(), "Tax (%.2f%%)\n%s%.2f", store.getTax(), store.getCurrencySymbol(), tax));
        binding.finalSpentTv.setText(String.format(Locale.getDefault(), "%s %.2f", store.getCurrencySymbol(), finalSpent));
        binding.remainingTv.setText(String.format(Locale.getDefault(), "%s %.2f", store.getCurrencySymbol(), remaining));
        binding.budgetTv.setText(String.format(Locale.getDefault(), "%s %.2f", store.getCurrencySymbol(), store.getBudget()));

        if (remaining < 0) {
            binding.remainingLabel.setText("Overspent");
            changeBudgetInfoColor(getResources().getColor(R.color.red, null));
        } else {
            binding.remainingLabel.setText("Remaining");
            changeBudgetInfoColor(getResources().getColor(R.color.light_green, null));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_menu, menu);
        menu.findItem(R.id.system_keyboard_menu).setChecked(Constants.USE_SYSTEM_KEYBOARD);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.store_name_menu) {
            new MyCustomDialog(this, this, DialogTask.EDIT_STORE_NAME).show();
            return true;
        } else if (id == R.id.budget_menu) {
            new MyCustomDialog(this, this, DialogTask.BUDGET).show();
            return true;
        } else if (id == R.id.tax_menu) {
            new MyCustomDialog(this, this, DialogTask.TAX).show();
            return true;
        } else if (id == R.id.discount_menu) {
            new MyCustomDialog(this, this, DialogTask.DISCOUNT).show();
            return true;
        } else if (id == R.id.system_keyboard_menu) {
            boolean isItemChecked = item.isChecked();
            item.setChecked(!isItemChecked);
            Constants.USE_SYSTEM_KEYBOARD = !isItemChecked;
            SharedPrefsUtils.saveData(this, SharedPrefsConstants.USE_SYSTEM_KEYBOARD, !isItemChecked);
        }
        return false;
    }

    private void setListeners() {
        binding.addItemBtn.setOnClickListener(view -> {
            changeDiscountAndTaxInfoVisibility(View.GONE);
            addItemDialog = new AddItemDialog(StoreActivity.this, StoreActivity.this);
            addItemDialog.setOnDismissListener(dialogInterface -> {
                if (Constants.currentStore.getDiscount() != 0 || Constants.currentStore.getTax() != 0) {
                    changeDiscountAndTaxInfoVisibility(View.VISIBLE);
                }
            });
            addItemDialog.show();
        });
    }

    private void changeBudgetInfoVisibility(int visibility) {
        binding.budgetTv.setVisibility(visibility);
        binding.budgetLabel.setVisibility(visibility);
        binding.remainingLabel.setVisibility(visibility);
        binding.remainingTv.setVisibility(visibility);
    }

    private void changeDiscountAndTaxInfoVisibility(int visibility) {
        binding.discountTv.setVisibility(visibility);
        binding.taxTv.setVisibility(visibility);
        binding.spentTv.setVisibility(visibility);
        binding.divider.setVisibility(visibility);
    }

    private void changeBudgetInfoColor(int color) {
        binding.remainingTv.setTextColor(color);
        binding.remainingLabel.setTextColor(color);
        binding.budgetLabel.setTextColor(color);
        binding.budgetTv.setTextColor(color);
    }

    @Override
    public void onDeleteItemClicked(Item currentItem) {
        new Builder(this)
                .setCancelable(false)
                .setMessage("Delete this item?")
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    int rowsAffected = db.itemDao().deleteItem(currentItem);
                    if (rowsAffected > 0) {
                        db.storeDao().updateTotalSpent(currentStoreId, store.getValue().getTotalSpent() - (currentItem.getPrice() * currentItem.getQuantity()));
                    }
                }).show();
    }

    @Override
    public void onQuantityChanged(Item currentItem, boolean isIncreased) {
        if (isIncreased) {
            Constants.currentStore.setTotalSpent(Constants.currentStore.getTotalSpent() + currentItem.getPrice());
        } else {
            Constants.currentStore.setTotalSpent(Constants.currentStore.getTotalSpent() - currentItem.getPrice());
        }
        if (!isIncreased && currentItem.getQuantity() == 0) { // Delete the item if quantity has become zero
            db.itemDao().deleteItem(currentItem);
        } else {
            db.itemDao().updateQuantity(currentItem.getId(), currentItem.getQuantity());
        }
        db.storeDao().updateTotalSpent(currentStoreId, Constants.currentStore.getTotalSpent());
    }


    @Override
    public void onPositiveClicked(String input, DialogTask dialogTask) {
        float previousTotalOfCurrentItem;
        switch (dialogTask) {
            case EDIT_STORE_NAME:
                db.storeDao().updateName(currentStoreId, input);
                setTitle(input);
                break;
            case BUDGET:
                float budget = Float.parseFloat(input);
                db.storeDao().updateBudget(currentStoreId, budget);
                break;
            case DISCOUNT:
                float discount = Float.parseFloat(input);
                db.storeDao().updateDiscount(currentStoreId, discount);
                break;
            case TAX:
                float tax = Float.parseFloat(input);
                db.storeDao().updateTax(currentStoreId, tax);
                break;
            case ITEM_QUANTITY:
                int quantity = Integer.parseInt(input);
                previousTotalOfCurrentItem = getTotalSpentOfCurrentItem();
                Constants.currentItem.setQuantity(quantity);
                db.itemDao().updateQuantity(Constants.currentItem.getId(), quantity);
                updateTotalSpent(previousTotalOfCurrentItem);
                break;
            case ITEM_PRICE:
                float price = Float.parseFloat(input);
                previousTotalOfCurrentItem = getTotalSpentOfCurrentItem();
                Constants.currentItem.setPrice(price);
                db.itemDao().updatePrice(Constants.currentItem.getId(), price);
                updateTotalSpent(previousTotalOfCurrentItem);
                break;
            case ITEM_NAME:
                Constants.currentItem.setName(input);
                db.itemDao().updateName(Constants.currentItem.getId(), input);
                break;
        }
    }


    private float getTotalSpentOfCurrentItem() {
        return Constants.currentItem.getQuantity() * Constants.currentItem.getPrice();
    }

    private void updateTotalSpent(float previousTotalOfCurrentItem) {
        float newTotalOfCurrentItem = getTotalSpentOfCurrentItem();
        db.storeDao().updateTotalSpent(currentStoreId, Constants.currentStore.getTotalSpent() - previousTotalOfCurrentItem + newTotalOfCurrentItem);
    }

    @Override
    public void onItemAdded(Item item) {
        if (item.getName().equals("")) {
            item.setName("Item " + (adapter.getItemCount() + 1));
        }
        db.itemDao().insertItem(item);
        db.storeDao().updateTotalSpent(currentStoreId, Constants.currentStore.getTotalSpent() + (item.getPrice() * item.getQuantity()));
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        FullScreenContentCallback contentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                finish();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                finish();
            }
        };
        AdUtils.showInterstitialAd(this, contentCallback, new OnLoadInterstitialFailedListener() {
            @Override
            public void onLoadInterstitialFailed() {
                finish();
            }
        });
    }
}
